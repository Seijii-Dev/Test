package io.zyxn

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import io.zyxn.api.InternalZyxnApi
import io.zyxn.api.NavDestination
import io.zyxn.api.Navigator
import io.zyxn.api.data.editor.FileOpenRequest
import io.zyxn.api.data.editor.FileOpener
import io.zyxn.api.data.editor.FileOpenerRegistration
import io.zyxn.api.data.editor.FileOpenerRegistry
import io.zyxn.api.data.editor.WorkspaceTab
import io.zyxn.api.data.fs.FileSystem
import io.zyxn.api.data.fs.Paths
import io.zyxn.api.data.fs.pluginsDir
import io.zyxn.api.data.runner.FileRunner
import io.zyxn.api.data.runner.FileRunRequest
import io.zyxn.api.data.runner.FileRunnerRegistration
import io.zyxn.api.data.runner.FileRunnerRegistry
import io.zyxn.api.data.terminal.TerminalManager
import io.zyxn.api.data.terminal.TerminalSessionBinder
import io.zyxn.api.data.terminal.TerminalSessionManager
import io.zyxn.api.event.EventBusHolder
import io.zyxn.api.language.LanguageRegistry
import io.zyxn.api.lsp.LanguageServerRegistry
import io.zyxn.api.plugin.ZyxnPlugin
import io.zyxn.api.plugin.PluginInfo
import io.zyxn.api.plugin.PluginSettings
import io.zyxn.api.plugin.PluginSettingsRegistration
import io.zyxn.api.plugin.PluginSettingsRegistry
import io.zyxn.api.plugin.info
import io.zyxn.api.service.Logger
import io.zyxn.api.ui.Content
import io.zyxn.api.ui.Screen
import io.zyxn.api.ui.ScreenId
import io.zyxn.api.ui.ScreenRegistration
import io.zyxn.api.ui.ScreenRegistry
import io.zyxn.api.ui.ToolbarAction
import io.zyxn.api.ui.ToolbarIcon
import io.zyxn.api.ui.ToolbarRegistration
import io.zyxn.api.ui.ToolbarRegistry
import io.zyxn.language.LanguageRegistryImpl
import io.zyxn.core.App
import io.zyxn.core.initApp
import io.zyxn.data.terminal.DefaultTerminalSessionManager
import io.zyxn.data.terminal.TerminalSessionBinderImpl
import io.zyxn.data.runner.PythonFileRunner
import io.zyxn.data.runner.TerminalCommandRunner
import io.zyxn.di.AppModule
import io.zyxn.event.eventBus
import io.zyxn.event.initializeGlobalEventBus
import io.zyxn.plugin.PluginManager
import io.zyxn.service.FontsWrapper
import io.zyxn.service.SettingsWrapper
import io.zyxn.service.TabsWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.plugin.module.dsl.startKoin

@KoinApplication(modules = [AppModule::class])
class ZyxnApplication : Application() {

    lateinit var app: App
        private set

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate() {
        super.onCreate()
        CrashHandler.install(this)
        System.loadLibrary("zyxn")

        startKoin<ZyxnApplication> {
            androidLogger()
            androidContext(this@ZyxnApplication)
        }

        app = initApp()
        initializeGlobals()
    }

    @OptIn(InternalZyxnApi::class)
    private fun initializeGlobals() {
        initializeGlobalEventBus(app)
        app.setGlobal(EventBusHolder(app.eventBus()))

        val terminalManager = TerminalManagerImpl(
            sessionBinder = TerminalSessionBinderImpl(),
            sessionManager = DefaultTerminalSessionManager(),
            terminalRunner = auto(),
            app = app,
        )
        app.setGlobal(terminalManager)
        app.setGlobal(auto<FileSystem>())
        app.setGlobal(MutableScreenRegistry())
        app.setGlobal(MutableToolbarRegistry())
        app.setGlobal(MutableFileRunnerRegistry().apply {
            registerInternal(PythonFileRunner())
        })
        app.setGlobal(MutableFileOpenerRegistry())
        app.setGlobal(MutablePluginSettingsRegistry())
        app.setGlobal(SettingsWrapper(auto()))
        app.setGlobal(FontsWrapper(auto()))
        app.setGlobal(TabsWrapper { auto() })
        app.setGlobal(PluginManager(app))
        app.setGlobal(auto<LanguageServerRegistry>())
        app.setGlobal(LanguageRegistryImpl(this))
        app.setGlobal(auto<Logger>())
    }

    private class TerminalManagerImpl(
        override val sessionManager: TerminalSessionManager,
        override val sessionBinder: TerminalSessionBinder,
        private val terminalRunner: TerminalCommandRunner,
        private val app: App,
    ) : TerminalManager {

        override suspend fun runInTerminal(
            command: String,
            cwd: String?,
            sessionName: String?,
        ) {
            terminalRunner.run(
                navigateToTerminal = { app.global<Navigator>().navigateTo(NavDestination.Terminal) },
                command = command,
                cwd = cwd,
                sessionName = sessionName,
            )
        }

        override fun openTerminal() {
            app.global<Navigator>().navigateTo(NavDestination.Terminal)
        }
    }

    private inline fun <reified T> auto(): T = GlobalContext.get().get()

    private class MutableScreenRegistry : ScreenRegistry {

        private val screens = mutableStateMapOf<ScreenId, Content>()
        private val transientScreens = mutableMapOf<ScreenId, Content>()
        private val screenOwner = mutableMapOf<ScreenId, String>()

        context(plugin: ZyxnPlugin)
        override fun register(screen: Screen): ScreenRegistration {
            transientScreens.remove(screen.id)
            screens[screen.id] = screen.content
            screenOwner[screen.id] = plugin.info.id
            return ScreenRegistration {
                screens.remove(screen.id)
                screenOwner.remove(screen.id)
            }
        }

        override fun unregister(id: ScreenId) {
            transientScreens.remove(id)
            screens.remove(id)
            screenOwner.remove(id)
        }

        override fun set(id: ScreenId, content: Content) {
            transientScreens.remove(id)
            screens[id] = content
        }

        override fun setTransient(id: ScreenId, content: Content) {
            transientScreens[id] = content
        }

        override fun unregisterTransient(id: ScreenId) {
            transientScreens.remove(id)
        }

        override fun get(id: ScreenId): Content? {
            return transientScreens[id] ?: screens[id]
        }

        override fun ownerOf(id: ScreenId): String? = screenOwner[id]

        @InternalZyxnApi
        override fun unregisterAll(pluginId: String) {
            val toRemove = screenOwner.filterValues { it == pluginId }.keys.toList()
            toRemove.forEach { unregister(it) }
        }
    }

    private class MutableToolbarRegistry : ToolbarRegistry {
        private val _actions = mutableStateListOf<ToolbarAction>()

        context(plugin: ZyxnPlugin)
        override fun register(action: ToolbarAction): ToolbarRegistration {
            val resolved = action.resolve(plugin.info)
            _actions += resolved
            return ToolbarRegistration { _actions.remove(resolved) }
        }

        fun ToolbarAction.resolve(info: PluginInfo): ToolbarAction {
            val resolved = when (val icon = icon) {
                is ToolbarIcon.Resource -> {
                    val file = Paths.pluginsDir
                        .resolve(info.id)
                        .resolve(icon.path)

                    if (file.exists()) {
                        ToolbarIcon.File(file)
                    } else {
                        Log.w("ToolbarRegistry", "Plugin '${info.id}' references missing icon '${icon.path}'.")
                        null
                    }
                }

                else -> icon
            }

            return copy(icon = resolved)
        }

        override fun unregister(id: String) {
            _actions.removeAll { it.id == id }
        }

        override fun actions(): List<ToolbarAction> {
            return _actions
        }
    }

    private class MutableFileRunnerRegistry : FileRunnerRegistry {

        private val _runners = mutableStateListOf<FileRunner>()
        private val _sortedRunners = mutableStateListOf<FileRunner>()
        private val _owners = mutableMapOf<String, String>()

        private fun updateSortedRunners() {
            _sortedRunners.clear()
            _sortedRunners.addAll(_runners.sortedByDescending { it.priority })
        }

        context(plugin: ZyxnPlugin)
        override fun register(runner: FileRunner): FileRunnerRegistration {
            _runners.removeAll { it.id == runner.id }
            _runners += runner
            updateSortedRunners()
            _owners[runner.id] = plugin.info.id
            return FileRunnerRegistration {
                _runners.removeAll { it.id == runner.id }
                updateSortedRunners()
                _owners.remove(runner.id)
            }
        }

        /**
         * Registers a built-in runner that is not owned by any plugin, so it is never removed
         * by [unregisterAll].
         */
        fun registerInternal(runner: FileRunner): FileRunnerRegistration {
            _runners.removeAll { it.id == runner.id }
            _runners += runner
            updateSortedRunners()
            return FileRunnerRegistration {
                _runners.removeAll { it.id == runner.id }
                updateSortedRunners()
                _owners.remove(runner.id)
            }
        }

        override fun unregister(id: String) {
            _runners.removeAll { it.id == id }
            updateSortedRunners()
            _owners.remove(id)
        }

        override fun runnerFor(request: FileRunRequest): FileRunner? =
            runners().firstOrNull { runCatching { it.supports(request) }.getOrDefault(false) }

        override fun supports(request: FileRunRequest): Boolean =
            runnerFor(request) != null

        override fun runners(): List<FileRunner> =
            _sortedRunners

        @InternalZyxnApi
        override fun unregisterAll(pluginId: String) {
            val toRemove = _owners.filterValues { it == pluginId }.keys.toList()
            toRemove.forEach { unregister(it) }
        }
    }

    private class MutableFileOpenerRegistry : FileOpenerRegistry {

        private val _openers = mutableStateListOf<FileOpener>()
        private val _sortedOpeners = mutableStateListOf<FileOpener>()

        private fun updateSortedOpeners() {
            _sortedOpeners.clear()
            _sortedOpeners.addAll(_openers.sortedByDescending { it.priority })
        }

        context(plugin: ZyxnPlugin)
        override fun register(opener: FileOpener): FileOpenerRegistration {
            _openers.removeAll { it.id == opener.id }
            _openers += opener
            updateSortedOpeners()
            return FileOpenerRegistration {
                _openers.removeAll { it.id == opener.id }
                updateSortedOpeners()
            }
        }

        override fun unregister(id: String) {
            _openers.removeAll { it.id == id }
            updateSortedOpeners()
        }

        override fun openers(): List<FileOpener> =
            _sortedOpeners

        override suspend fun open(request: FileOpenRequest): WorkspaceTab? {
            for (opener in openers()) {
                val tab = opener.open(request)
                if (tab != null) return tab
            }
            return null
        }
    }

    private class MutablePluginSettingsRegistry : PluginSettingsRegistry {

        private val content = mutableStateMapOf<String, @Composable PluginSettings.() -> Unit>()

        context(plugin: ZyxnPlugin)
        override fun register(
            content: @Composable PluginSettings.() -> Unit
        ): PluginSettingsRegistration {
            val pluginId = plugin.info.id
            this.content[pluginId] = content
            return PluginSettingsRegistration { this@MutablePluginSettingsRegistry.content.remove(pluginId) }
        }

        override fun hasSettings(pluginId: String): Boolean = content.containsKey(pluginId)

        @InternalZyxnApi
        override fun contentFor(pluginId: String): (@Composable PluginSettings.() -> Unit)? =
            content[pluginId]

        @InternalZyxnApi
        override fun unregisterAll(pluginId: String) {
            content.remove(pluginId)
        }
    }
}

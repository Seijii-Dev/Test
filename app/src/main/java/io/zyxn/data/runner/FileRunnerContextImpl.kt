package io.zyxn.data.runner

import io.zyxn.api.data.editor.WorkspaceTab
import io.zyxn.api.data.runner.FileRunnerContext
import io.zyxn.api.ui.Content
import io.zyxn.api.ui.ScreenId
import io.zyxn.api.ui.ScreenRegistration
import io.zyxn.api.ui.ScreenRegistry
import io.zyxn.presentation.navigation.Navigator
import io.zyxn.presentation.navigation.Screen

internal class FileRunnerContextImpl(
    private val terminalRunner: TerminalCommandRunner,
    private val navigator: Navigator,
    private val screenRegistry: ScreenRegistry,
    private val openTab: (WorkspaceTab) -> Unit,
) : FileRunnerContext {

    override suspend fun runInTerminal(
        command: String,
        cwd: String?,
        sessionName: String?,
    ) {
        terminalRunner.run(
            navigateToTerminal = { navigator.navigateTo(Screen.Terminal) },
            command = command,
            cwd = cwd,
            sessionName = sessionName,
        )
    }

    override fun openTerminal() {
        navigator.navigateTo(Screen.Terminal)
    }

    override fun openScreen(screenId: ScreenId) {
        navigator.navigateTo(Screen.Custom(screenId))
    }

    override fun openScreen(screenId: ScreenId, content: Content): ScreenRegistration {
        screenRegistry.setTransient(screenId, content)
        navigator.navigateTo(Screen.Custom(screenId))
        return ScreenRegistration { screenRegistry.unregisterTransient(screenId) }
    }

    override fun openTab(tab: WorkspaceTab) {
        openTab.invoke(tab)
    }
}

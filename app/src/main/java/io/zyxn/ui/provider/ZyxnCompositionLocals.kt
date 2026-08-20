package io.zyxn.ui.provider

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.zyxn.core.App
import io.zyxn.core.LocalApp
import io.zyxn.data.editor.LocalZyxnEditorColorScheme
import io.zyxn.data.editor.rememberEditorColorScheme
import io.zyxn.api.data.preferences.AppSettings
import io.zyxn.api.data.preferences.AppTheme
import io.zyxn.api.data.preferences.LocalAppSettings
import io.zyxn.api.language.LanguageRegistry
import io.zyxn.data.preferences.SettingsRepository
import io.zyxn.event.eventBus
import io.zyxn.language.LanguageRegistryImpl
import io.zyxn.ui.ImmersiveModeHandler
import io.zyxn.ui.animation.LocalReduceMotion
import io.zyxn.api.ui.theme.LocalIsDarkMode
import io.zyxn.i18n.ProvideStrings
import io.zyxn.i18n.rememberStrings
import io.zyxn.ui.theme.ZyxnThemeSurface
import io.zyxn.ui.widgets.ToastHost
import org.koin.compose.currentKoinScope
import org.koin.compose.koinInject

@Composable
fun ZyxnCompositionLocals(content: @Composable BoxScope.() -> Unit) {
    val screenSize = rememberScreenSize()
    val treeSitter = rememberTreeSitter()

    val app: App = currentKoinScope().get()

    val settingsRepository: SettingsRepository = koinInject()
    val settings by settingsRepository.settings.collectAsStateWithLifecycle(initialValue = AppSettings())

    val isSystemInDarkTheme = isSystemInDarkTheme()
    val appTheme by settingsRepository.appTheme.collectAsStateWithLifecycle(initialValue = AppTheme.System)

    val darkMode by remember {
        derivedStateOf {
            when (appTheme) {
                AppTheme.Light -> false
                AppTheme.Dark -> true
                AppTheme.System -> isSystemInDarkTheme
            }
        }
    }

    val editorColorScheme = rememberEditorColorScheme()

    val values by remember {
        derivedStateOf {
            arrayOf(
                LocalScreenSize provides screenSize,
                LocalTreeSitter provides treeSitter,
                LocalIsDarkMode provides darkMode,
                LocalAppSettings provides settings,
                LocalReduceMotion provides settings.appearance.reduceMotion,
                LocalZyxnEditorColorScheme provides editorColorScheme,
                LocalApp provides app,
                LocalEventBus provides app.eventBus()
            )
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val languageRegistry = app.globalOrNull<LanguageRegistry>() as? LanguageRegistryImpl

    DisposableEffect(lifecycleOwner, treeSitter) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                treeSitter.close()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        languageRegistry?.bind(treeSitter)
        onDispose {
            languageRegistry?.unbind()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    CompositionLocalProvider(values = values) {
        ImmersiveModeHandler(
            isImmersiveModeEnabled = settings.appearance.immersiveMode
        ) {
            val languageTag = settings.appearance.language.languageTag
                ?: Locale.current.toLanguageTag()
            ProvideStrings(rememberStrings(currentLanguageTag = languageTag)) {
                ZyxnThemeSurface {
                    content()
                    ToastHost(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

package io.zyxn.ui.theme

import android.os.Build
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import io.zyxn.api.data.preferences.LocalAppSettings
import io.zyxn.api.ui.theme.LocalIsDarkMode
import io.zyxn.api.ui.theme.Typography
import io.zyxn.api.ui.theme.backgroundDark
import io.zyxn.api.ui.theme.backgroundLight
import io.zyxn.api.ui.theme.errorContainerDark
import io.zyxn.api.ui.theme.errorContainerLight
import io.zyxn.api.ui.theme.errorDark
import io.zyxn.api.ui.theme.errorLight
import io.zyxn.api.ui.theme.inverseOnSurfaceDark
import io.zyxn.api.ui.theme.inverseOnSurfaceLight
import io.zyxn.api.ui.theme.inversePrimaryDark
import io.zyxn.api.ui.theme.inversePrimaryLight
import io.zyxn.api.ui.theme.inverseSurfaceDark
import io.zyxn.api.ui.theme.inverseSurfaceLight
import io.zyxn.api.ui.theme.onBackgroundDark
import io.zyxn.api.ui.theme.onBackgroundLight
import io.zyxn.api.ui.theme.onErrorContainerDark
import io.zyxn.api.ui.theme.onErrorContainerLight
import io.zyxn.api.ui.theme.onErrorDark
import io.zyxn.api.ui.theme.onErrorLight
import io.zyxn.api.ui.theme.onPrimaryContainerDark
import io.zyxn.api.ui.theme.onPrimaryContainerLight
import io.zyxn.api.ui.theme.onPrimaryDark
import io.zyxn.api.ui.theme.onPrimaryLight
import io.zyxn.api.ui.theme.onSecondaryContainerDark
import io.zyxn.api.ui.theme.onSecondaryContainerLight
import io.zyxn.api.ui.theme.onSecondaryDark
import io.zyxn.api.ui.theme.onSecondaryLight
import io.zyxn.api.ui.theme.onSurfaceDark
import io.zyxn.api.ui.theme.onSurfaceLight
import io.zyxn.api.ui.theme.onSurfaceVariantDark
import io.zyxn.api.ui.theme.onSurfaceVariantLight
import io.zyxn.api.ui.theme.onTertiaryContainerDark
import io.zyxn.api.ui.theme.onTertiaryContainerLight
import io.zyxn.api.ui.theme.onTertiaryDark
import io.zyxn.api.ui.theme.onTertiaryLight
import io.zyxn.api.ui.theme.outlineDark
import io.zyxn.api.ui.theme.outlineLight
import io.zyxn.api.ui.theme.outlineVariantDark
import io.zyxn.api.ui.theme.outlineVariantLight
import io.zyxn.api.ui.theme.primaryContainerDark
import io.zyxn.api.ui.theme.primaryContainerLight
import io.zyxn.api.ui.theme.primaryDark
import io.zyxn.api.ui.theme.primaryLight
import io.zyxn.api.ui.theme.scrimDark
import io.zyxn.api.ui.theme.scrimLight
import io.zyxn.api.ui.theme.secondaryContainerDark
import io.zyxn.api.ui.theme.secondaryContainerLight
import io.zyxn.api.ui.theme.secondaryDark
import io.zyxn.api.ui.theme.secondaryLight
import io.zyxn.api.ui.theme.surfaceBrightDark
import io.zyxn.api.ui.theme.surfaceBrightLight
import io.zyxn.api.ui.theme.surfaceContainerDark
import io.zyxn.api.ui.theme.surfaceContainerHighDark
import io.zyxn.api.ui.theme.surfaceContainerHighLight
import io.zyxn.api.ui.theme.surfaceContainerHighestDark
import io.zyxn.api.ui.theme.surfaceContainerHighestLight
import io.zyxn.api.ui.theme.surfaceContainerLight
import io.zyxn.api.ui.theme.surfaceContainerLowDark
import io.zyxn.api.ui.theme.surfaceContainerLowLight
import io.zyxn.api.ui.theme.surfaceContainerLowestDark
import io.zyxn.api.ui.theme.surfaceContainerLowestLight
import io.zyxn.api.ui.theme.surfaceDark
import io.zyxn.api.ui.theme.surfaceDimDark
import io.zyxn.api.ui.theme.surfaceDimLight
import io.zyxn.api.ui.theme.surfaceLight
import io.zyxn.api.ui.theme.surfaceVariantDark
import io.zyxn.api.ui.theme.surfaceVariantLight
import io.zyxn.api.ui.theme.tertiaryContainerDark
import io.zyxn.api.ui.theme.tertiaryContainerLight
import io.zyxn.api.ui.theme.tertiaryDark
import io.zyxn.api.ui.theme.tertiaryLight
import io.zyxn.ui.animation.LocalReduceMotion
import io.zyxn.ui.animation.orSnap

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private fun ColorScheme.applyAmoled(): ColorScheme {
    return this.copy(
        background = Color.Black,
        surface = Color.Black,
        surfaceContainerLowest = Color.Black,
        surfaceContainerLow = Color(0xFF0A0A0A),
        surfaceContainer = Color(0xFF121212)
    )
}

@Composable
fun ZyxnTheme(
    darkTheme: Boolean = LocalIsDarkMode.current,
    amoled: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    val reduceMotion = LocalReduceMotion.current
    val finalColorScheme = if (darkTheme && amoled) colorScheme.applyAmoled() else colorScheme

    MaterialExpressiveTheme(
        colorScheme = finalColorScheme,
        typography = Typography,
        motionScheme = reducedMotionScheme(reduceMotion),
        content = content
    )
}

private fun reducedMotionScheme(reduceMotion: Boolean) = object : MotionScheme {

    val expressive = MotionScheme.expressive()

    override fun <T> defaultSpatialSpec(): FiniteAnimationSpec<T> {
        return expressive.defaultSpatialSpec<T>().orSnap(reduceMotion)
    }

    override fun <T> fastSpatialSpec(): FiniteAnimationSpec<T> {
        return expressive.fastSpatialSpec<T>().orSnap(reduceMotion)
    }

    override fun <T> slowSpatialSpec(): FiniteAnimationSpec<T> {
        return expressive.slowSpatialSpec<T>().orSnap(reduceMotion)
    }

    override fun <T> defaultEffectsSpec(): FiniteAnimationSpec<T> {
        return expressive.defaultEffectsSpec<T>().orSnap(reduceMotion)
    }

    override fun <T> fastEffectsSpec(): FiniteAnimationSpec<T> {
        return expressive.fastEffectsSpec<T>().orSnap(reduceMotion)
    }

    override fun <T> slowEffectsSpec(): FiniteAnimationSpec<T> {
        return expressive.slowEffectsSpec<T>().orSnap(reduceMotion)
    }
}

@Composable
fun ZyxnThemeSurface(content: @Composable BoxScope.() -> Unit) {
    ZyxnTheme(amoled = LocalAppSettings.current.appearance.amoledDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            content = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    content = content
                )
            }
        )
    }
}

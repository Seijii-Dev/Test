package io.zyxn.api.language

fun interface LanguageThemeProvider {
    fun getStyleForCapture(captureName: String): CaptureStyle?
}

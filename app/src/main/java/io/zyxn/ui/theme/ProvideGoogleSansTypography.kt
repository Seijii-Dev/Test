package io.zyxn.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import io.zyxn.api.ui.theme.GoogleSansTypography

@Composable
fun ProvideGoogleSansTypography(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = GoogleSansTypography,
        content = content
    )
}

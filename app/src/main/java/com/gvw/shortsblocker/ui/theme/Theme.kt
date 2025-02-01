package com.gvw.shortsblocker.ui.theme

import androidx.compose.material3.*
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

val Typography = Typography()

@Composable
fun StopShortVideosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = Typography,
        content = content
    )
}


// Preview to check the theme
@Preview
@Composable
fun PreviewTheme() {
    StopShortVideosTheme {
        Text("Hello, Theme!")
    }
}

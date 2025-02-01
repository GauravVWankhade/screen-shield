package com.gvw.shortsblocker.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun StopShortVideosTheme(content: @Composable () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()  // Automatically switch between light and dark themes

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme().copy(
            primary = Color(0xFF6200EE) // Custom primary color (can be adjusted as needed)
        ),
        content = content
    )
}

// Preview of the Theme
@Preview(showBackground = true)
@Composable
fun PreviewTheme() {
    StopShortVideosTheme {
        Text("Hello, Theme!", style = MaterialTheme.typography.displayLarge)
    }
}

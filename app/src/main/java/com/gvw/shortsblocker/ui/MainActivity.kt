package com.gvw.shortsblocker.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.gvw.shortsblocker.ui.theme.StopShortVideosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopShortVideosTheme {
                AppUI()
            }
        }
    }
}

@Composable
fun AppUI() {
    var isEnabled by remember { mutableStateOf(false) }

    // Simulate checking if the accessibility service is enabled (for demonstration)
    // You can replace this with actual logic to check the status
    LaunchedEffect(Unit) {
        isEnabled = checkIfAccessibilityServiceIsEnabled() // Simulated function
    }

    val context = LocalContext.current // Get the current context here

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Stop Short Videos", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        if (isEnabled) {
            Text(text = "Accessibility Service is Enabled!", style = MaterialTheme.typography.bodyLarge)
        } else {
            Button(onClick = {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                context.startActivity(intent) // Use context to start the activity
            }) {
                Text("Enable Accessibility Service")
            }
        }
    }
}

// Simulated function to check if the accessibility service is enabled
fun checkIfAccessibilityServiceIsEnabled(): Boolean {
    // Replace with actual logic to check service status
    return false // For testing, returns false to show "Enable Accessibility Service" button
}

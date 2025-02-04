package com.gvw.shortsblocker.ui

import android.content.BroadcastReceiver
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
import android.content.Context
import android.content.IntentFilter
import android.text.TextUtils
import androidx.compose.ui.graphics.Color
import com.gvw.shortsblocker.accessibility.VideoBlockerService

class MainActivity : ComponentActivity() {
    private lateinit var receiver: AccessibilityServiceReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isEnabled by remember { mutableStateOf(false) }
            val context = LocalContext.current

            receiver = AccessibilityServiceReceiver { enabled ->
                isEnabled = enabled
            }

            // Register the receiver to listen for Accessibility Service updates
            DisposableEffect(Unit) {
                val filter = IntentFilter("com.gvw.shortsblocker.ACCESSIBILITY_STATUS")
                context.registerReceiver(receiver, filter)
                onDispose {
                    context.unregisterReceiver(receiver)
                }
            }

            StopShortVideosTheme {
                AppUI(isEnabled, context)
            }
        }
    }
}


@Composable
fun AppUI(isEnabled: Boolean, context: Context) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Stop Short Videos",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (isEnabled) {
            Text(
                text = "Accessibility Service is Enabled!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Green
            )
        } else {
            Button(onClick = {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                context.startActivity(intent)
            }) {
                Text("Enable Accessibility Service")
            }
        }
    }
}


// Simulated function to check if the accessibility service is enabled
fun checkIfAccessibilityServiceIsEnabled(context: Context, serviceClass: Class<*>): Boolean {
    val expectedComponentName = "${context.packageName}/${serviceClass.name}"
    val enabledServices = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    ) ?: return false

    val colonSplitter = TextUtils.SimpleStringSplitter(':')
    colonSplitter.setString(enabledServices)
    while (colonSplitter.hasNext()) {
        if (colonSplitter.next().equals(expectedComponentName, ignoreCase = true)) {
            return true
        }
    }
    return Settings.Secure.getInt(
        context.contentResolver,
        Settings.Secure.ACCESSIBILITY_ENABLED, 0
    ) == 1
}

class AccessibilityServiceReceiver(private val onStatusChanged: (Boolean) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val isEnabled = intent?.getBooleanExtra("isEnabled", false) ?: false
        onStatusChanged(isEnabled)
    }
}

package com.gvw.shortsblocker.ui

import AccessibilityStatusReceiver
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.content.IntentFilter
import android.provider.Settings
import com.gvw.shortsblocker.ui.theme.StopShortVideosTheme

class MainActivity : ComponentActivity() {

    private lateinit var receiver: AccessibilityStatusReceiver

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                val intent = Intent("com.gvw.shortsblocker.REQUEST_NOTIFICATION_PERMISSION")
                sendBroadcast(intent)
            } else {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("package:${packageName}")
                )
                startActivity(intent)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isEnabled by remember { mutableStateOf(false) }
            val context = LocalContext.current

            // Receiver listens for changes in service status
            receiver = AccessibilityStatusReceiver { enabled ->
                isEnabled = enabled
            }

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

    private val notificationPermissionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(notificationPermissionReceiver, IntentFilter("com.gvw.shortsblocker.REQUEST_NOTIFICATION_PERMISSION"))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(notificationPermissionReceiver)
    }
}

@Composable
fun AppUI(isEnabled: Boolean, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
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
            // If the service is enabled, show a confirmation text
            Text(
                text = "Accessibility Service is Enabled!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Green
            )
        } else {
            // If the service is not enabled, show the button to open Accessibility settings
            Button(onClick = {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                context.startActivity(intent)
            }) {
                Text("Enable Accessibility Service")
            }
        }
    }
}

package com.gvw.shortsblocker.ui

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
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gvw.shortsblocker.receiver.AccessibilityStatusReceiver
import com.gvw.shortsblocker.ui.theme.StopShortVideosTheme

class MainActivity : ComponentActivity() {
    private lateinit var screenReceiver: BroadcastReceiver
    private val isEnabled: Boolean
        get() = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getBoolean("is_enabled", false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopShortVideosTheme {
                StopShortVideosTheme {
                    AppUI(isEnabled, context=this)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerScreenReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterScreenReceiver()
    }

    private fun registerScreenReceiver() {
        screenReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_SCREEN_ON) {
                    Log.d("MainActivity", "Screen turned on")
                }
            }
        }

        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(screenReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(screenReceiver, filter)
        }
    }


    private fun unregisterScreenReceiver() {
        try {
            unregisterReceiver(screenReceiver)
        } catch (e: IllegalArgumentException) {
            Log.e("MainActivity", "Receiver not registered", e)
        }
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

package com.gvw.shortsblocker.receiver

import PersistentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AccessibilityStatusReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val isEnabled = intent.getBooleanExtra("isEnabled", false)
        Log.d("AccessibilityStatusReceiver", "Service status changed: $isEnabled")

        if (isEnabled) {
            startPersistentService(context)
        }

        sendAccessibilityStatusBroadcast(context, isEnabled)
    }

    private fun startPersistentService(context: Context) {
        val serviceIntent = Intent(context, PersistentService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)  // Required for Android 8+
        } else {
            context.startService(serviceIntent)
        }
    }

    private fun sendAccessibilityStatusBroadcast(context: Context, isEnabled: Boolean) {
        val intent = Intent().apply {
            action = "com.gvw.shortsblocker.ACCESSIBILITY_STATUS"
            setPackage(context.packageName)  // Ensure explicit broadcast
            putExtra("isEnabled", isEnabled)
        }
        context.sendBroadcast(intent)
    }
}

package com.gvw.shortsblocker.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat

class VideoBlockerService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        sendServiceStatus(true)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Log events for debugging
        Log.d("VideoBlockerService", "Event received: ${event.packageName}")

        // Add logic to detect short videos and take action
    }

    override fun onInterrupt() {
        Log.d("VideoBlockerService", "Service Interrupted!")
    }

    override fun onDestroy() {
        super.onDestroy()
        sendServiceStatus(false)
    }

    private fun sendServiceStatus(isEnabled: Boolean) {
        val intent = Intent("com.gvw.shortsblocker.ACCESSIBILITY_STATUS")
        intent.putExtra("isEnabled", isEnabled)
        sendBroadcast(intent)
    }
}


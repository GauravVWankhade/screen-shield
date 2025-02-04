package com.gvw.shortsblocker.accessibility

import android.Manifest
import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.gvw.shortsblocker.ui.MainActivity
import com.gvw.shortsblocker.R

class VideoBlockerService : AccessibilityService() {

    companion object {
        private const val CHANNEL_ID = "shorts_blocker_service_channel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        sendServiceStatus(true)
        showStatusBarIcon()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        Log.d("VideoBlockerService", "Event received: ${event.packageName}")
        performGlobalAction(GLOBAL_ACTION_BACK)
    }

    override fun onInterrupt() {
        Log.d("VideoBlockerService", "Service Interrupted!")
    }

    override fun onDestroy() {
        super.onDestroy()
        sendServiceStatus(false)
        stopForeground(true) // Remove the icon from the status bar
    }

    private fun sendServiceStatus(isEnabled: Boolean) {
        val intent = Intent("com.gvw.shortsblocker.ACCESSIBILITY_STATUS")
        intent.putExtra("isEnabled", isEnabled)
        sendBroadcast(intent)
    }

    private fun showStatusBarIcon() {
        Log.d("VideoBlockerService", "Showing status bar icon")

        // Create a notification channel (required for Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Shorts Blocker Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Check for notification permission on Android 13 (TIRAMISU) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                val intent = Intent("com.gvw.shortsblocker.REQUEST_NOTIFICATION_PERMISSION")
                sendBroadcast(intent)
            } else {
                // Permission granted, proceed with the notification
                showNotification()
            }
        } else {
            // For Android versions below TIRAMISU, no need for permission check
            showNotification()
        }
    }

    private fun showNotification() {
        // Create a simple notification with an icon for the status bar
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ShortsBlocker is Running")
            .setContentText("Blocking short videos in the background")
            .setSmallIcon(R.drawable.ic_status_icon) // Ensure this is a valid icon
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Use HIGH priority
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Make it visible
            .setOngoing(true) // Prevent users from dismissing it
            .build()

        // Start the foreground service to show the icon in the status bar
        startForeground(NOTIFICATION_ID, notification)
    }
}

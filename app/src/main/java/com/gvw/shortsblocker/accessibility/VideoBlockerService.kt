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
import com.gvw.shortsblocker.R
import com.gvw.shortsblocker.ui.MainActivity
import com.gvw.shortsblocker.ui.PermissionRequestActivity



class VideoBlockerService : AccessibilityService() {

    companion object {
        private const val CHANNEL_ID = "shorts_blocker_service_channel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val intent = Intent(
            this,
            PermissionRequestActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Required when starting from a service
        startActivity(intent)
        sendAccessibilityStatusBroadcast(true)  // Accessibility Service is enabled
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
        sendAccessibilityStatusBroadcast(false)  // Accessibility Service is disabled
        stopForeground(true) // Remove the icon from the status bar
    }

    override fun onUnbind(intent: Intent?): Boolean {
        sendAccessibilityStatusBroadcast(false)  // Accessibility Service is disabled
        return super.onUnbind(intent)
    }

    private fun sendAccessibilityStatusBroadcast(isEnabled: Boolean) {
        val intent = Intent("com.gvw.shortsblocker.ACCESSIBILITY_STATUS")
        intent.putExtra("isEnabled", isEnabled)
        sendBroadcast(intent)
    }

    private fun showStatusBarIcon() {
        Log.d("VideoBlockerService", "Showing status bar icon")

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                val intent = Intent("com.gvw.shortsblocker.REQUEST_NOTIFICATION_PERMISSION")
                sendBroadcast(intent)
                showNotification()
            } else {
                showNotification()
            }
        } else {
            showNotification()
        }
    }

    private fun showNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setAllowSystemGeneratedContextualActions(true)
            .setContentTitle("ShortsBlocker is Running")
            .setContentText("Blocking short videos in the background")
            .setSmallIcon(R.drawable.ic_status_icon)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }
}

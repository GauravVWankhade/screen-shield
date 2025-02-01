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
        Log.d("VideoBlockerService", "Service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            val packageName = it.packageName.toString()
            Log.d("VideoBlockerService", "App detected: $packageName")

            // Check if the app is YouTube or Instagram
            if (packageName == "com.google.android.youtube" || packageName == "com.instagram.android") {
                Log.d("VideoBlockerService", "Blocking videos for package: $packageName")

                // Try to stop video by simulating the HOME key or back navigation
                 performGlobalAction(GLOBAL_ACTION_HOME)
                // stopVideoPlayback()
                // Alternatively, you could simulate a BACK action as well:
                // performGlobalAction(GLOBAL_ACTION_BACK)
            }
        }
    }

    override fun onInterrupt() {
        // Called when the service is interrupted
        Log.d("VideoBlockerService", "Service interrupted")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("VideoBlockerService", "Service unbound")
        return super.onUnbind(intent)
    }

//    private fun stopVideoPlayback() {
//        val rootNode = rootInActiveWindow
//        rootNode?.let { node ->
//            val nodes = findVideoControls(node)
//            for (controlNode in nodes) {
//                if (controlNode.text.toString().contains("pause", ignoreCase = true)) {
//                    controlNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//                    Log.d("VideoBlockerService", "Clicked on pause button")
//                }
//            }
//        }
//    }
//
//    private fun findVideoControls(node: AccessibilityNodeInfo): List<AccessibilityNodeInfo> {
//        val nodes = mutableListOf<AccessibilityNodeInfo>()
//        if (node.childCount > 0) {
//            for (i in 0 until node.childCount) {
//                val childNode = node.getChild(i)
//                if (childNode != null) {
//                    if (childNode.text != null && childNode.text.toString().contains("play", ignoreCase = true)) {
//                        nodes.add(childNode)
//                    }
//                    nodes.addAll(findVideoControls(childNode)) // Recursive search for child nodes
//                }
//            }
//        }
//        return nodes
//    }

}

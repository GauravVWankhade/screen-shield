package com.gvw.shortsblocker.ui

import PersistentService
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.media.projection.MediaProjectionManager

class PermissionRequestActivity : Activity() {

    private val REQUEST_MEDIA_PROJECTION = 1001  // Request Code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val intent = mediaProjectionManager.createScreenCaptureIntent()
        startActivityForResult(intent, REQUEST_MEDIA_PROJECTION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == RESULT_OK && data != null) {
                // Permission granted, start your foreground service
                val serviceIntent = Intent(this, PersistentService::class.java)
                serviceIntent.putExtra("media_projection_data", data)
                startForegroundService(serviceIntent)
            } else {
                // Permission denied, handle it
                finish()
            }
        }
    }
}

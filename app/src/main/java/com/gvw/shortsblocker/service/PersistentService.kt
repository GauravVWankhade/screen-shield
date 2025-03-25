import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.gvw.shortsblocker.R

class PersistentService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val mediaProjectionData = intent?.getParcelableExtra<Intent>("media_projection_data")

        if (mediaProjectionData != null) {
            val mediaProjectionManager = getSystemService(MediaProjectionManager::class.java)
            val mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, mediaProjectionData)

            // TODO: Use mediaProjection to capture screen content
        }

        startForegroundServiceWithNotification()
        return START_STICKY
    }

    private fun startForegroundServiceWithNotification() {
        val channelId = "shorts_blocker_service"
        val channelName = "Shorts Blocker Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Shorts Blocker Running")
            .setContentText("Blocking short videos in background")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION)
        } else {
            startForeground(1, notification)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

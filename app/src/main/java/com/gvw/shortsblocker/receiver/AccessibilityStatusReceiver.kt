
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AccessibilityStatusReceiver(private val onStatusChanged: (Boolean) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val isEnabled = intent?.getBooleanExtra("isEnabled", false) ?: false
        onStatusChanged(isEnabled)
    }
}


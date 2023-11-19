package fr.wonderfulappstudio.notifymehere.presentation.service

import android.content.Intent
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import fr.wonderfulappstudio.notifymehere.presentation.MainActivity

class DataLayerListenerService: WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)

        when (messageEvent.path) {
            START_ACTIVITY_PATH -> {
                startActivity(
                    Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

    companion object {
        private const val START_ACTIVITY_PATH = "/start-activity"
        const val INTEREST_POINTS_PATH = "/interest-points"
        const val INTEREST_POINTS_KEY = "interestPoints"
    }
}

package com.kproject.simplechat.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kproject.simplechat.R
import com.kproject.simplechat.model.ReceivedMessage
import com.kproject.simplechat.ui.activities.MainActivity

@ExperimentalCoilApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
class FirebaseService : FirebaseMessagingService() {
    private val CHANNEL_ID = "my_notification_channel"

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // The notification will only be displayed if the app is in the background
        if (isAppInBackground()) {
            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]
            val fromUserId = remoteMessage.data["fromUserId"]
            val fromUserName = remoteMessage.data["fromUserName"]
            val userProfileImage = remoteMessage.data["userProfileImage"]

            val intent = Intent(this, MainActivity::class.java)
            val receivedMessage = ReceivedMessage(
                fromUserId = fromUserId!!,
                userName = fromUserName!!,
                userProfileImage = userProfileImage!!
            )
            intent.putExtra("receivedMessage", receivedMessage)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = fromUserId.hashCode()
            createNotificationChannel(notificationManager)

            val pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            else
                PendingIntent.FLAG_UPDATE_CURRENT
            val pendingIntent =
                    PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        pendingIntentFlag or PendingIntent.FLAG_ONE_SHOT
                    )

            /**
             * About current notification display behavior:
             * If a notification with the same id is already displayed, new notifications will
             * replace the current notification. This is not good behavior. In the future it
             * should be replaced to stack new messages in the same notification instead of just
             * displaying the most recent notification.
             */
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_email)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(notificationId, notification)
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.chat_notification_name)
            val descriptionText = getString(R.string.chat_notification_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun isAppInBackground(): Boolean {
        return ProcessLifecycleOwner.get().lifecycle.currentState == Lifecycle.State.CREATED
    }
}
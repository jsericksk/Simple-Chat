package com.kproject.simplechat.presentation.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kproject.simplechat.R
import com.kproject.simplechat.presentation.MainActivity
import com.kproject.simplechat.presentation.model.ChatMessageNotification

class NotificationService : FirebaseMessagingService() {
    private val CHANNEL_ID = "chat_message_notification_channel"

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (isAppInBackground()) {
            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]
            val fromUserId = remoteMessage.data["fromUserId"]
            val fromUserName = remoteMessage.data["fromUsername"]
            val userProfilePicture = remoteMessage.data["userProfilePicture"]

            val intent = Intent(this, MainActivity::class.java)
            val chatMessageNotification = ChatMessageNotification(
                fromUserId = fromUserId!!,
                username = fromUserName!!,
                userProfilePicture = userProfilePicture!!
            )
            intent.putExtra("chatMessageNotification", chatMessageNotification)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = fromUserId.hashCode()
            createNotificationChannel(notificationManager)

            val pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent, pendingIntentFlag or PendingIntent.FLAG_ONE_SHOT
            )

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
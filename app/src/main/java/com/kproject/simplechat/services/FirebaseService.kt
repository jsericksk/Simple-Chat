package com.kproject.simplechat.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kproject.simplechat.R
import com.kproject.simplechat.model.ReceivedMessage
import com.kproject.simplechat.ui.activities.MainActivity
import com.kproject.simplechat.utils.DataStoreUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        val title = remoteMessage.data["title"]
        var message = remoteMessage.data["message"]
        val fromUserId = remoteMessage.data["fromUserId"]
        val fromUserName = remoteMessage.data["fromUserName"]
        val userProfileImage = remoteMessage.data["userProfileImage"]

        Log.d("FirebaseService", "From user ID: $fromUserId")

        val intent = Intent(this, MainActivity::class.java)
        val receivedMessage = ReceivedMessage(
            fromUserId = fromUserId!!,
            userName = fromUserName!!,
            userProfileImage = userProfileImage!!
        )
        intent.putExtra("receivedMessage", receivedMessage)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = fromUserId.hashCode()
        createNotificationChannel(notificationManager)

        /**
         * Gets possible previous messages from that same user that are
         * already showing in the notification.
         */
        val previousMessage = getNotificationMessage(notificationId = notificationId)
        if (previousMessage.isNotEmpty()) {
            message = "$previousMessage\n$message"
        }
        saveOrResetNotificationMessageTemporarily(
            saveMessage = false,
            notificationId = notificationId
        )

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_email)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            /**.setStyle(NotificationCompat.InboxStyle()
                .addLine(messageSnippet1)
                .addLine(messageSnippet2))*/
            .build()

        saveOrResetNotificationMessageTemporarily(
            saveMessage = true,
            notificationId = notificationId,
            notificationMessage = message!!
        )

        notificationManager.notify(notificationId, notification)
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

    private fun saveOrResetNotificationMessageTemporarily(
        saveMessage: Boolean,
        notificationId: Int,
        notificationMessage: String = ""
    ) {
        val sharedPreferences = this.getSharedPreferences("notification_messages", Context.MODE_PRIVATE)
        val message = if (saveMessage) notificationMessage else ""
        sharedPreferences.edit().putString(notificationId.toString(), message).apply()
    }

    private fun getNotificationMessage(notificationId: Int): String {
        val sharedPreferences = this.getSharedPreferences("notification_messages", Context.MODE_PRIVATE)
        return sharedPreferences.getString(notificationId.toString(), "")!!
    }

    class Notification {

    }
}
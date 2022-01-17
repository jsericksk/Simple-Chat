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
import com.kproject.simplechat.data.network.models.MessageNotificationData
import com.kproject.simplechat.ui.activities.MainActivity
import kotlin.random.Random

class FirebaseService : FirebaseMessagingService() {
    val CHANNEL_ID = "my_notification_channel"

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
    }

    @ExperimentalCoilApi
    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]
        val fromUserId = remoteMessage.data["fromUserId"]

        Log.d("FirebaseService", "From user ID: $fromUserId")

        val intent = Intent(this, MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = fromUserId.hashCode()
        createNotificationChannel(notificationManager)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_email)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificatdionChannel(notificationManager: NotificationManager){
        val channelName = "ChannelFirebaseChat"
        val channel = NotificationChannel(CHANNEL_ID,channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "MY FIREBASE CHAT DESCRIPTION"
            enableLights(true)
            // lightColor = Color.WHITE
        }
        notificationManager.createNotificationChannel(channel)
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
}
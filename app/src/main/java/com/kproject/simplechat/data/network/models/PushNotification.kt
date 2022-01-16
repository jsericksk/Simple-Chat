package com.kproject.simplechat.data.network.models

data class PushNotification(
    var data: MessageNotificationData,
    var to: String
)
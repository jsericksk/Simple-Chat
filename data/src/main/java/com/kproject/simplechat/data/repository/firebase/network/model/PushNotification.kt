package com.kproject.simplechat.data.repository.firebase.network.model

import com.kproject.simplechat.data.model.ChatMessageNotificationEntity

data class PushNotification(
    val data: ChatMessageNotificationEntity,
    val to: String
)

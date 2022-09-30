package com.kproject.simplechat.data.repository.firebase.network.model

data class PushNotification(
    val data: ChatMessageNotification,
    val to: String
)

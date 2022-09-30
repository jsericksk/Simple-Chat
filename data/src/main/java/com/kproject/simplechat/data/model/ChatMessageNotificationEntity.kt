package com.kproject.simplechat.data.model

data class ChatMessageNotificationEntity(
    val title: String,
    val message: String,
    val fromUserId: String,
    val fromUsername: String,
    val userProfilePicture: String
)
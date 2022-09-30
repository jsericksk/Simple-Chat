package com.kproject.simplechat.domain.model.firebase

data class ChatMessageNotificationModel(
    val title: String,
    val message: String,
    val fromUserId: String,
    val fromUsername: String,
    val userProfilePicture: String
)

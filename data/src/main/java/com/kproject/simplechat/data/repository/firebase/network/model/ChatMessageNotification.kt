package com.kproject.simplechat.data.repository.firebase.network.model

data class ChatMessageNotification(
    val title: String,
    val message: String,
    val fromUserId: String,
    val fromUsername: String,
    val userProfilePicture: String
)
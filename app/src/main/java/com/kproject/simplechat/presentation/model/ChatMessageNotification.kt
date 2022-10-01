package com.kproject.simplechat.presentation.model

import java.io.Serializable

data class ChatMessageNotification(
    val fromUserId: String,
    val username: String,
    val userProfilePicture: String
) : Serializable
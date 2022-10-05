package com.kproject.simplechat.presentation.model

import com.kproject.simplechat.presentation.utils.Utils
import java.util.*

data class LatestMessage(
    val chatId: String = "",
    val latestMessage: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val username: String = "",
    val userProfilePicture: String = "",
    val timestamp: Date? = null
) {
    val formattedDate = Utils.getChatMessageFormattedDate(timestamp)
}

val fakeLatestMessagesList = (0..20).map { index ->
    val latestMessage = if (index % 2 == 0) "Hello my friend" else "Hi!"
    val senderId = if (index % 2 == 0) "123456" else "123456789"
    val username = if (index % 2 == 0) "Simple" else "Chat"
    LatestMessage(
        chatId = "123456",
        latestMessage = latestMessage,
        senderId = senderId,
        receiverId = "12345678$index",
        username = username,
        userProfilePicture = "",
        timestamp = null
    )
}
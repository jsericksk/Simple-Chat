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
    LatestMessage(
        chatId = "123456",
        latestMessage = "$index. Hello my friend",
        senderId = "12345$index",
        receiverId = "12345678$index",
        username = "Simple Chat",
        userProfilePicture = "",
        timestamp = null
    )
}
package com.kproject.simplechat.presentation.model

import com.kproject.simplechat.presentation.utils.Utils
import java.util.*

data class LatestMessage(
    var chatId: String = "",
    var latestMessage: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var username: String = "",
    var userProfilePicture: String = "",
    var timestamp: Date? = null
) {
    val formattedDate = Utils.getFormattedDate(timestamp)
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
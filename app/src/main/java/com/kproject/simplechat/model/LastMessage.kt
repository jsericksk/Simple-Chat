package com.kproject.simplechat.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class LastMessage(
    var chatId: String = "",
    var lastMessage: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var userName: String = "",
    var userProfileImage: String = "",
    @ServerTimestamp
    var timestamp: Date? = null
)
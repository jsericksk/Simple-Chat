package com.kproject.simplechat.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class LatestMessageEntity(
    var chatId: String = "",
    var latestMessage: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var username: String = "",
    var userProfilePicture: String = "",
    @ServerTimestamp
    var timestamp: Date? = null
)
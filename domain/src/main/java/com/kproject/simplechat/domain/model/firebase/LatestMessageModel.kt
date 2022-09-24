package com.kproject.simplechat.domain.model.firebase

import java.util.*

data class LatestMessageModel(
    var chatId: String = "",
    var latestMessage: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var username: String = "",
    var userProfilePicture: String = "",
    var timestamp: Date? = null
)
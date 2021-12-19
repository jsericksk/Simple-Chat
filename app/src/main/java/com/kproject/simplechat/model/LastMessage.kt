package com.kproject.simplechat.model

data class LastMessage(
    var chatId: String = "",
    var lastMessage: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var userName: String = "",
    var userProfileImage: String = "",
    var timestamp: Long = 0
)
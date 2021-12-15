package com.kproject.simplechat.model

data class Message(
    var message: String = "",
    var senderId: String = "",
    var receiverId: String = "",
)
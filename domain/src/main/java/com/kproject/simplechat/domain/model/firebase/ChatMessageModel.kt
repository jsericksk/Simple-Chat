package com.kproject.simplechat.domain.model.firebase

import java.util.*

data class ChatMessageModel(
    val message: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val sendDate: Date? = null
)
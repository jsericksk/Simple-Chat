package com.kproject.simplechat.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class ChatMessageEntity(
    val message: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    @ServerTimestamp
    val sendDate: Date? = null
)
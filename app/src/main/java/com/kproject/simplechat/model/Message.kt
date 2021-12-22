package com.kproject.simplechat.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    var message: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    @ServerTimestamp var timestamp: Date? = null
)
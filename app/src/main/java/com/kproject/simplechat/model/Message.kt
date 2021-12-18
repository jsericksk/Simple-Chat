package com.kproject.simplechat.model

import com.google.firebase.firestore.FieldValue

data class Message(
    var message: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var timestamp: Long
)
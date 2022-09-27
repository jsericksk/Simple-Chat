package com.kproject.simplechat.presentation.model

import com.kproject.simplechat.presentation.utils.Utils
import java.util.*

data class ChatMessage(
    val message: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val sendDate: Date? = null
) {
    val formattedDate = Utils.getChatMessageFormattedDate(sendDate)
}

val fakeChatMessagesList = (0..20).map { index ->
    val message = if (index % 2 == 0) {
        "Hello, how are you?"
    } else {
        "I'm fine and you? :)"
    }
    val senderId = if (index % 2 == 0) "123456" else "123456789"

    ChatMessage(
        message = message,
        senderId = senderId,
        receiverId = "123456$index"
    )
}


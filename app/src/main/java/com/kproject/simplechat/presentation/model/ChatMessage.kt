package com.kproject.simplechat.presentation.model

import com.kproject.simplechat.presentation.utils.Utils
import java.util.*

data class ChatMessage(
    var message: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var sendDate: Date? = null
) {
    val formattedDate = Utils.getChatMessageFormattedDate(sendDate)
}

val fakeChatMessagesList = (0..20).map { index ->
    val message = if (index % 2 == 0) {
        "Hello, how are you?"
    } else {
        "I'm fine and you? :)"
    }

    ChatMessage(
        message = message,
        senderId = "12345$index",
        receiverId = "12345678$index"
    )
}

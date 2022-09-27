package com.kproject.simplechat.presentation.screens.chat

import com.kproject.simplechat.presentation.model.ChatMessage
import com.kproject.simplechat.presentation.model.User

data class ChatUiState(
    val chatMessageList: List<ChatMessage> = emptyList(),
    val user: User = User(),
    val message: String = "",
)
package com.kproject.simplechat.presentation.screens.home.latestmessages

import com.kproject.simplechat.presentation.model.LatestMessage

data class LatestMessagesUiState(
    val latestMessagesList: List<LatestMessage> = emptyList()
)
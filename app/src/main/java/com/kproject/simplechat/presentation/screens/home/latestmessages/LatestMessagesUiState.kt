package com.kproject.simplechat.presentation.screens.home.latestmessages

import com.kproject.simplechat.presentation.model.LatestMessage
import com.kproject.simplechat.presentation.model.fakeLatestMessagesList

data class LatestMessagesUiState(
    val latestMessages: List<LatestMessage> = emptyList(),
    val loggedUserId: String = ""
)
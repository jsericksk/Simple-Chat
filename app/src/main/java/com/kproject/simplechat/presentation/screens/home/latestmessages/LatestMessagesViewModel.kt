package com.kproject.simplechat.presentation.screens.home.latestmessages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.presentation.model.fakeLatestMessagesList

class LatestMessagesViewModel(

) : ViewModel() {
    var uiState by mutableStateOf(LatestMessagesUiState(
        latestMessagesList = fakeLatestMessagesList
    ))
        private set

    var dataState: DataState<Unit>? by mutableStateOf(null)
        private set


}
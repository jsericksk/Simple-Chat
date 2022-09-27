package com.kproject.simplechat.presentation.screens.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.presentation.model.ChatMessage
import com.kproject.simplechat.presentation.model.User
import com.kproject.simplechat.presentation.model.fakeChatMessagesList
import com.kproject.simplechat.presentation.screens.authentication.login.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var uiState by mutableStateOf(ChatUiState())
        private set

    var dataState: DataState<Unit> by mutableStateOf(DataState.Loading)
        private set

    init {
        getMessages()

        // val ak = savedStateHandle.get<String>("kkfkf")
    }

    private fun getMessages() {
        viewModelScope.launch {
            delay(2000)
            dataState = DataState.Success()
            uiState = uiState.copy(
                chatMessageList = fakeChatMessagesList
            )
        }
    }

    fun initializeUser(user: User) {
        uiState = uiState.copy(user = user)
    }
}
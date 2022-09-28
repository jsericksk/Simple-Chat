package com.kproject.simplechat.presentation.screens.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.usecase.firebase.chat.GetMessagesUseCase
import com.kproject.simplechat.domain.usecase.firebase.chat.SendMessageUseCase
import com.kproject.simplechat.presentation.mapper.toChatMessage
import com.kproject.simplechat.presentation.mapper.toChatMessageModel
import com.kproject.simplechat.presentation.model.ChatMessage
import com.kproject.simplechat.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {
    var uiState by mutableStateOf(ChatUiState())
        private set

    var dataState: DataState<Unit> by mutableStateOf(DataState.Loading)
        private set

    fun getMessages(fromUserId: String) {
        viewModelScope.launch {
            getMessagesUseCase(fromUserId).collect { result ->
                when (result) {
                    is DataState.Success -> {
                        dataState = DataState.Success()
                        result.data?.let { chatMessages ->
                            val messages = chatMessages.map { chatMessageModel ->
                                chatMessageModel.toChatMessage()
                            }
                            uiState = uiState.copy(chatMessageList = messages)
                        }
                    }
                    is DataState.Error -> {
                        dataState = DataState.Error()
                    }
                    else -> {}
                }
            }
        }
    }

    fun sendMessage(chatMessage: ChatMessage) {
        viewModelScope.launch {
            val message = chatMessage.toChatMessageModel()
            sendMessageUseCase(message)
            uiState = uiState.copy(message = "")
        }
    }

    fun initializeUser(user: User) {
        uiState = uiState.copy(user = user)
    }

    fun onMessageChange(message: String) {
        uiState = uiState.copy(message = message)
    }
}
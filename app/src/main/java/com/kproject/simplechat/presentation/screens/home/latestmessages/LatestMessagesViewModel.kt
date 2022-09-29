package com.kproject.simplechat.presentation.screens.home.latestmessages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.usecase.firebase.user.GetLatestMessagesUseCase
import com.kproject.simplechat.domain.usecase.firebase.user.GetLoggedUserIdUseCase
import com.kproject.simplechat.presentation.mapper.toLatestMessage
import com.kproject.simplechat.presentation.model.LatestMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LatestMessagesViewModel"

@HiltViewModel
class LatestMessagesViewModel @Inject constructor(
    private val getLatestMessagesUseCase: GetLatestMessagesUseCase,
    private val getLoggedUserIdUseCase: GetLoggedUserIdUseCase
) : ViewModel() {
    var uiState by mutableStateOf(LatestMessagesUiState())
        private set

    var dataState: DataState<List<LatestMessage>> by mutableStateOf(DataState.Success())
        private set

    init {
        getLoggedUserIdUseCase()?.let { loggedUserId ->
            uiState = uiState.copy(loggedUserId = loggedUserId)
        }
        getLatestMessages()
    }

    private fun getLatestMessages() {
        viewModelScope.launch {
            dataState = DataState.Loading
            getLatestMessagesUseCase().collect { result ->
                when (result) {
                    is DataState.Success -> {
                        dataState = DataState.Success()
                        result.data?.let { messages ->
                            val latestMessages = messages.map { latestMessageModel ->
                                latestMessageModel.toLatestMessage()
                            }
                            uiState = uiState.copy(latestMessages = latestMessages)
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
}
package com.kproject.simplechat.presentation.screens.home.registeredusers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.usecase.firebase.user.GetRegisteredUsersUseCase
import com.kproject.simplechat.presentation.mapper.toUser
import com.kproject.simplechat.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "RegisteredUsersViewModel"

@HiltViewModel
class RegisteredUsersViewModel @Inject constructor(
    private val getRegisteredUsersUseCase: GetRegisteredUsersUseCase
) : ViewModel() {
    var uiState by mutableStateOf(RegisteredUsersUiState())
        private set

    var dataState: DataState<List<User>> by mutableStateOf(DataState.Loading)
        private set

    init {
        getRegisteredUsers()
    }

    private fun getRegisteredUsers() {
        viewModelScope.launch {
            dataState = DataState.Loading
            getRegisteredUsersUseCase().collect { result ->
                when (result) {
                    is DataState.Success -> {
                        dataState = DataState.Success()
                        result.data?.let { users ->
                            val registeredUsers = users.map { userModel ->
                                userModel.toUser()
                            }
                            uiState = uiState.copy(registeredUsers = registeredUsers)
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
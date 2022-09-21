package com.kproject.simplechat.presentation.screens.home.registeredusers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.presentation.model.User

class RegisteredUsersViewModel(

) : ViewModel() {
    var uiState by mutableStateOf(RegisteredUsersUiState())
        private set

    var dataState: DataState<List<User>> by mutableStateOf(DataState.Success())
        private set


}
package com.kproject.simplechat.presentation.screens.home.registeredusers

import com.kproject.simplechat.presentation.model.User

data class RegisteredUsersUiState(
    val registeredUsersList: List<User> = emptyList()
)
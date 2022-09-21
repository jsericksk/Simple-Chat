package com.kproject.simplechat.presentation.screens.home.registeredusers

import com.kproject.simplechat.presentation.model.User
import com.kproject.simplechat.presentation.model.fakeRegisteredUsersList

data class RegisteredUsersUiState(
    val registeredUsersList: List<User> = fakeRegisteredUsersList // emptyList()
)
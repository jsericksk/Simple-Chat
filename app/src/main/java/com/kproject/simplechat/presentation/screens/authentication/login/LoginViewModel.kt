package com.kproject.simplechat.presentation.screens.authentication.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kproject.simplechat.commom.exception.AuthenticationException
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidateEmailUseCase
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {
    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(email: String) {
        loginUiState = loginUiState.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        loginUiState = loginUiState.copy(password = password)
    }

    fun login() {

    }
}
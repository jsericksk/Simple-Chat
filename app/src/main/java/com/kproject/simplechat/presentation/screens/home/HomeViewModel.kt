package com.kproject.simplechat.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.usecase.firebase.authentication.LogoutUseCase
import com.kproject.simplechat.domain.usecase.firebase.user.GetCurrentUserUseCase
import com.kproject.simplechat.domain.usecase.firebase.user.GetLoggedUserIdUseCase
import com.kproject.simplechat.domain.usecase.preferences.GetPreferenceAsyncUseCase
import com.kproject.simplechat.domain.usecase.preferences.GetPreferenceSyncUseCase
import com.kproject.simplechat.domain.usecase.preferences.SavePreferenceUseCase
import com.kproject.simplechat.presentation.mapper.toUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getLoggedUserIdUseCase: GetLoggedUserIdUseCase,
    private val getPreferenceAsyncUseCase: GetPreferenceAsyncUseCase,
    private val getPreferenceSyncUseCase: GetPreferenceSyncUseCase,
    private val savePreferenceUseCase: SavePreferenceUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val result = getCurrentUserUseCase()
            if (result is DataState.Success) {
                result.data?.let { userModel ->
                    uiState = uiState.copy(user = userModel.toUser())
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}

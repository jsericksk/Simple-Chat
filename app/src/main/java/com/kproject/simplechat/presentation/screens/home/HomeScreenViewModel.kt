package com.kproject.simplechat.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.commom.constants.PrefsConstants
import com.kproject.simplechat.domain.usecase.preferences.GetPreferenceAsyncUseCase
import com.kproject.simplechat.domain.usecase.preferences.GetPreferenceSyncUseCase
import com.kproject.simplechat.domain.usecase.preferences.SavePreferenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getPreferenceAsyncUseCase: GetPreferenceAsyncUseCase,
    private val getPreferenceSyncUseCase: GetPreferenceSyncUseCase,
    private val savePreferenceUseCase: SavePreferenceUseCase
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        getUserProfilePicture()
    }

    private fun getUserProfilePicture() {
        viewModelScope.launch {
            getPreferenceAsyncUseCase(
                key = PrefsConstants.UserProfilePicture,
                defaultValue = ""
            ).collectLatest {
                uiState = uiState.copy(userProfilePicture = it as String)
            }
        }
    }
}

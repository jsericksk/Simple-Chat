package com.kproject.simplechat.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.commom.constants.PrefsConstants
import com.kproject.simplechat.domain.usecase.preferences.GetPreferenceAsyncUseCase
import com.kproject.simplechat.domain.usecase.preferences.SavePreferenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPreferenceAsyncUseCase: GetPreferenceAsyncUseCase,
    private val savePreferenceUseCase: SavePreferenceUseCase
) : ViewModel() {
    var isDarkMode by mutableStateOf(true)
        private set
    var isUserLoggedIn by mutableStateOf(false)
        private set

    private var isDarkModePreferenceCollected = false
    private var isUserLoggedInPreferenceCollected = false

    init {
        viewModelScope.launch {
            collectThemeChanges()
            isUserLeggedIn()
        }
    }

    private fun collectThemeChanges() {
        viewModelScope.launch {
            getPreferenceAsyncUseCase(
                key = PrefsConstants.IsDarkMode,
                defaultValue = true
            ).collectLatest {
                isDarkMode = it as Boolean
                isDarkModePreferenceCollected = true
            }
        }
    }

    private fun isUserLeggedIn() {
        viewModelScope.launch {
            getPreferenceAsyncUseCase(
                key = PrefsConstants.IsUserLoggedIn,
                defaultValue = false
            ).collectLatest {
                isUserLoggedIn = it as Boolean
                isUserLoggedInPreferenceCollected = true
            }
        }
    }

    fun isContentReady(): Boolean {
        if (isDarkModePreferenceCollected && isUserLoggedInPreferenceCollected) {
            return true
        }
        return false
    }

    fun changeTheme() {
        viewModelScope.launch {
            savePreferenceUseCase(
                key = PrefsConstants.IsDarkMode,
                value = !isDarkMode
            )
        }
    }
}
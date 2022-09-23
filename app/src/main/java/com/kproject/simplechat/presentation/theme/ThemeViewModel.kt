package com.kproject.simplechat.presentation.theme

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
class ThemeViewModel @Inject constructor(
    private val getPreferenceAsyncUseCase: GetPreferenceAsyncUseCase,
    private val getPreferenceSyncUseCase: GetPreferenceSyncUseCase,
    private val savePreferenceUseCase: SavePreferenceUseCase
) : ViewModel() {
    var isDarkMode by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            getPreferenceAsyncUseCase(
                key = PrefsConstants.IsDarkMode,
                defaultValue = true
            ).collectLatest {
                isDarkMode = it as Boolean
            }
        }
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
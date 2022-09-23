package com.kproject.simplechat.presentation.theme

import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ThemeViewModel"

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
            getCurrentThemeSync()
            // Collect changes in theme mode
            getPreferenceAsyncUseCase(
                key = PrefsConstants.IsDarkMode,
                defaultValue = true
            ).collectLatest {
                isDarkMode = it as Boolean
            }
        }
    }

    /**
     * Gets the current theme synchronously. This is useful so that there are no inconsistencies
     * in the theme display the first time, if isDarkMode is false, as there will be a small
     * delay until the first collection of the current theme.
     */
    private fun getCurrentThemeSync() {
        viewModelScope.launch {
            try {
                isDarkMode = getPreferenceSyncUseCase(
                    key = PrefsConstants.IsDarkMode,
                    defaultValue = true
                ) as Boolean
            } catch (e: Exception) {
                Log.e(TAG, "Error getPreferenceSyncUseCase(): ${e.message}")
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
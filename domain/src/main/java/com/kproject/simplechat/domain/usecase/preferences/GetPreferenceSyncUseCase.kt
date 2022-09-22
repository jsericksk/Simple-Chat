package com.kproject.simplechat.domain.usecase.preferences

import kotlinx.coroutines.flow.Flow

interface GetPreferenceSyncUseCase {
    fun getPreferenceAsync(key: String, defaultValue: Any): Any
}
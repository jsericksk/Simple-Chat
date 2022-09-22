package com.kproject.simplechat.domain.usecase.preferences

interface GetPreferenceSyncUseCase {
    fun getPreferenceAsync(key: String, defaultValue: Any): Any
}
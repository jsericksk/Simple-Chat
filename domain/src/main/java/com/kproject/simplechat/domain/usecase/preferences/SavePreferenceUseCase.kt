package com.kproject.simplechat.domain.usecase.preferences

interface SavePreferenceUseCase {
    suspend fun savePreference(key: String, value: Any)
}
package com.kproject.simplechat.domain.usecase.preferences

interface SavePreferenceUseCase {
    suspend operator fun invoke(key: String, value: Any)
}
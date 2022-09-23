package com.kproject.simplechat.domain.usecase.preferences

import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository

class SavePreferenceUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
) : SavePreferenceUseCase {

    override suspend operator fun invoke(key: String, value: Any) {
        dataStoreRepository.savePreference(key, value)
    }
}
package com.kproject.simplechat.domain.usecase.preferences

import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository

class GetPreferenceSyncUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
) : GetPreferenceSyncUseCase {

    override fun getPreferenceAsync(key: String, defaultValue: Any): Any {
        return dataStoreRepository.getPreferenceSync(key, defaultValue)
    }
}
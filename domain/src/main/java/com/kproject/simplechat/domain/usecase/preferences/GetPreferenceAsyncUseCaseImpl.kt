package com.kproject.simplechat.domain.usecase.preferences

import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetPreferenceAsyncUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
) : GetPreferenceAsyncUseCase {

    override suspend fun getPreferenceAsync(key: String, defaultValue: Any): Flow<Any> {
        return dataStoreRepository.getPreferenceAsync(key, defaultValue)
    }
}
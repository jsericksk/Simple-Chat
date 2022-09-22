package com.kproject.simplechat.domain.usecase.preferences

import kotlinx.coroutines.flow.Flow

interface GetPreferenceAsyncUseCase {
    suspend fun getPreferenceAsync(key: String, defaultValue: Any): Flow<Any>
}
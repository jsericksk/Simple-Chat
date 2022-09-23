package com.kproject.simplechat.domain.usecase.preferences

import kotlinx.coroutines.flow.Flow

interface GetPreferenceAsyncUseCase {
    suspend operator fun invoke(key: String, defaultValue: Any): Flow<Any>
}
package com.kproject.simplechat.domain.repository.preferences

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun getPreferenceAsync(key: String, defaultValue: Any): Flow<Any>

    fun getPreferenceSync(key: String, defaultValue: Any): Any

    suspend fun savePreference(key: String, value: Any)
}
package com.kproject.simplechat.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object DataStoreUtils {

    suspend fun savePreference(context: Context, key: String, value: String) {
        val keyPreferences = stringPreferencesKey(key)
        context.dataStore.edit {
            it[keyPreferences] = value
        }
    }

    suspend fun savePreference(context: Context, key: String, value: Boolean) {
        val keyPreferences = booleanPreferencesKey(key)
        context.dataStore.edit {
            it[keyPreferences] = value
        }
    }

    suspend fun savePreference(context: Context, key: String, value: Int) {
        val keyPreferences = intPreferencesKey(key)
        context.dataStore.edit {
            it[keyPreferences] = value
        }
    }

    fun readPreference(context: Context, key: String, defaultValue: String): Flow<String> {
        val keyPreferences = stringPreferencesKey(key)
        return context.dataStore.data.map {
            it[keyPreferences] ?: defaultValue
        }
    }

    fun readPreference(context: Context, key: String, defaultValue: Boolean): Flow<Boolean> {
        val keyPreferences = booleanPreferencesKey(key)
        return context.dataStore.data.map {
            it[keyPreferences] ?: defaultValue
        }
    }

    fun readPreference(context: Context, key: String, defaultValue: Int): Flow<Int> {
        val keyPreferences = intPreferencesKey(key)
        return context.dataStore.data.map {
            it[keyPreferences] ?: defaultValue
        }
    }

    fun readPreferenceWithoutFlow(context: Context, key: String, defaultValue: String): String {
        val keyPreferences = stringPreferencesKey(key)
        return runBlocking {
            context.dataStore.data.map {
                it[keyPreferences] ?: defaultValue
            }.first()
        }
    }

    fun readPreferenceWithoutFlow(context: Context, key: String, defaultValue: Boolean): Boolean {
        val keyPreferences = booleanPreferencesKey(key)
        return runBlocking {
            context.dataStore.data.map {
                it[keyPreferences] ?: defaultValue
            }.first()
        }
    }

    fun readPreferenceWithoutFlow(context: Context, key: String, defaultValue: Int): Int {
        val keyPreferences = intPreferencesKey(key)
        return runBlocking {
            context.dataStore.data.map {
                it[keyPreferences] ?: defaultValue
            }.first()
        }
    }
}

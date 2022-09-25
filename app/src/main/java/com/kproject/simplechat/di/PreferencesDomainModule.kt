package com.kproject.simplechat.di

import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository
import com.kproject.simplechat.domain.usecase.preferences.GetPreferenceAsyncUseCase
import com.kproject.simplechat.domain.usecase.preferences.GetPreferenceSyncUseCase
import com.kproject.simplechat.domain.usecase.preferences.SavePreferenceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesDomainModule {

    @Provides
    @Singleton
    fun provideGetPreferenceAsyncUseCase(dataStoreRepository: DataStoreRepository): GetPreferenceAsyncUseCase {
        return GetPreferenceAsyncUseCase(dataStoreRepository::getPreferenceAsync)
    }

    @Provides
    @Singleton
    fun provideGetPreferenceSyncUseCase(dataStoreRepository: DataStoreRepository): GetPreferenceSyncUseCase {
        return GetPreferenceSyncUseCase(dataStoreRepository::getPreferenceSync)
    }

    @Provides
    @Singleton
    fun provideSavePreferenceUseCase(dataStoreRepository: DataStoreRepository): SavePreferenceUseCase {
        return SavePreferenceUseCase(dataStoreRepository::savePreference)
    }
}
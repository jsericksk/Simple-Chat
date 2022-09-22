package com.kproject.simplechat.di

import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository
import com.kproject.simplechat.domain.usecase.preferences.*
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
        return GetPreferenceAsyncUseCaseImpl(dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideGetPreferenceSyncUseCase(dataStoreRepository: DataStoreRepository): GetPreferenceSyncUseCase {
        return GetPreferenceSyncUseCaseImpl(dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideSavePreferenceUseCase(dataStoreRepository: DataStoreRepository): SavePreferenceUseCase {
        return SavePreferenceUseCaseImpl(dataStoreRepository)
    }
}
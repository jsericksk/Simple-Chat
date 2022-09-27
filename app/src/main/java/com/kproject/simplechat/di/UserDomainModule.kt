package com.kproject.simplechat.di

import com.kproject.simplechat.domain.repository.firebase.UserRepository
import com.kproject.simplechat.domain.usecase.user.GetCurrentUserUseCase
import com.kproject.simplechat.domain.usecase.user.GetLatestMessagesUseCase
import com.kproject.simplechat.domain.usecase.user.GetRegisteredUsersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDomainModule {

    @Provides
    @Singleton
    fun provideGetRegisteredUsersUseCase(userRepository: UserRepository): GetRegisteredUsersUseCase {
        return GetRegisteredUsersUseCase(userRepository::getRegisteredUsers)
    }

    @Provides
    @Singleton
    fun provideGetLatestMessagesUseCase(userRepository: UserRepository): GetLatestMessagesUseCase {
        return GetLatestMessagesUseCase(userRepository::getLatestMessages)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(userRepository: UserRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(userRepository::getCurrentUser)
    }
}
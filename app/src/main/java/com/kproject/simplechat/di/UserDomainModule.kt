package com.kproject.simplechat.di

import com.kproject.simplechat.domain.repository.firebase.UserRepository
import com.kproject.simplechat.domain.usecase.firebase.user.GetCurrentUserUseCase
import com.kproject.simplechat.domain.usecase.firebase.user.GetLatestMessagesUseCase
import com.kproject.simplechat.domain.usecase.firebase.user.GetLoggedUserIdUseCase
import com.kproject.simplechat.domain.usecase.firebase.user.GetRegisteredUsersUseCase
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

    @Provides
    @Singleton
    fun provideGetLoggedUserIdUseCase(userRepository: UserRepository): GetLoggedUserIdUseCase {
        return GetLoggedUserIdUseCase(userRepository::getLoggedUserId)
    }
}
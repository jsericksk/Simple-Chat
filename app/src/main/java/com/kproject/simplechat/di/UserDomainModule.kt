package com.kproject.simplechat.di

import com.kproject.simplechat.domain.repository.firebase.UserRepository
import com.kproject.simplechat.domain.usecase.user.GetRegisteredUsersUseCase
import com.kproject.simplechat.domain.usecase.user.GetRegisteredUsersUseCaseImpl
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
        return GetRegisteredUsersUseCaseImpl(userRepository)
    }

}

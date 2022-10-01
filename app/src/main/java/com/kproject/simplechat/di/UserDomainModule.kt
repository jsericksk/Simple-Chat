package com.kproject.simplechat.di

import com.kproject.simplechat.domain.repository.firebase.PushNotificationRepository
import com.kproject.simplechat.domain.repository.firebase.UserRepository
import com.kproject.simplechat.domain.usecase.firebase.user.*
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

    @Provides
    @Singleton
    fun provideSubscribeToTopicUseCase(pushNotificationRepository: PushNotificationRepository): SubscribeToTopicUseCase {
        return SubscribeToTopicUseCase(pushNotificationRepository::subscribeToTopic)
    }

    @Provides
    @Singleton
    fun provideUnsubscribeFromTopicUseCase(pushNotificationRepository: PushNotificationRepository): UnsubscribeFromTopicUseCase {
        return UnsubscribeFromTopicUseCase(pushNotificationRepository::unsubscribeFromTopic)
    }
}
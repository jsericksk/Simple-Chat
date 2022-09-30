package com.kproject.simplechat.di

import com.kproject.simplechat.domain.repository.firebase.ChatRepository
import com.kproject.simplechat.domain.repository.firebase.PushNotificationRepository
import com.kproject.simplechat.domain.usecase.firebase.chat.GetMessagesUseCase
import com.kproject.simplechat.domain.usecase.firebase.chat.PostNotificationUseCase
import com.kproject.simplechat.domain.usecase.firebase.chat.SaveLatestMessageUseCase
import com.kproject.simplechat.domain.usecase.firebase.chat.SendMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatDomainModule {

    @Provides
    @Singleton
    fun provideGetMessagesUseCase(chatRepository: ChatRepository): GetMessagesUseCase {
        return GetMessagesUseCase(chatRepository::getMessages)
    }

    @Provides
    @Singleton
    fun provideSendMessageUseCase(chatRepository: ChatRepository): SendMessageUseCase {
        return SendMessageUseCase(chatRepository::sendMessage)
    }

    @Provides
    @Singleton
    fun provideSaveLatestMessageUseCase(chatRepository: ChatRepository): SaveLatestMessageUseCase {
        return SaveLatestMessageUseCase(chatRepository::saveLatestMessage)
    }

    @Provides
    @Singleton
    fun providePostNotificationUseCase(pushNotificationRepository: PushNotificationRepository): PostNotificationUseCase {
        return PostNotificationUseCase(pushNotificationRepository::postNotification)
    }
}
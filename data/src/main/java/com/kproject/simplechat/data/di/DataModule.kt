package com.kproject.simplechat.data.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kproject.simplechat.data.repository.firebase.AuthenticationRepositoryImpl
import com.kproject.simplechat.data.repository.firebase.ChatRepositoryImpl
import com.kproject.simplechat.data.repository.firebase.PushNotificationRepositoryImpl
import com.kproject.simplechat.data.repository.firebase.UserRepositoryImpl
import com.kproject.simplechat.data.repository.firebase.network.PushNotificationApiService
import com.kproject.simplechat.data.repository.preferences.DataStoreRepositoryImpl
import com.kproject.simplechat.data.utils.Constants
import com.kproject.simplechat.domain.repository.firebase.AuthenticationRepository
import com.kproject.simplechat.domain.repository.firebase.ChatRepository
import com.kproject.simplechat.domain.repository.firebase.PushNotificationRepository
import com.kproject.simplechat.domain.repository.firebase.UserRepository
import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext applicationContext: Context): DataStoreRepository {
        return DataStoreRepositoryImpl(applicationContext)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        dataStoreRepository: DataStoreRepository
    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(firebaseAuth, firebaseFirestore, dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        dataStoreRepository: DataStoreRepository
    ): UserRepository {
        return UserRepositoryImpl(firebaseAuth, firebaseFirestore, dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        firebaseFirestore: FirebaseFirestore,
        userRepository: UserRepository
    ): ChatRepository {
        return ChatRepositoryImpl(firebaseFirestore, userRepository)
    }

    @Provides
    @Singleton
    fun providePushNotificationApiService(): PushNotificationApiService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.FCMBaseUrl)
            .build()
        return retrofit.create(PushNotificationApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePushNotificationRepository(
        pushNotificationApiService: PushNotificationApiService,
        dataStoreRepository: DataStoreRepository
    ): PushNotificationRepository {
        return PushNotificationRepositoryImpl(pushNotificationApiService, dataStoreRepository)
    }
}
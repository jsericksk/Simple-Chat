package com.kproject.simplechat.di

import com.kproject.simplechat.domain.repository.authentication.AuthenticationRepository
import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository
import com.kproject.simplechat.domain.usecase.authentication.login.LoginUseCase
import com.kproject.simplechat.domain.usecase.authentication.login.LoginUseCaseImpl
import com.kproject.simplechat.domain.usecase.authentication.signup.SignUpUseCase
import com.kproject.simplechat.domain.usecase.authentication.signup.SignUpUseCaseImpl
import com.kproject.simplechat.domain.usecase.authentication.validation.*
import com.kproject.simplechat.domain.usecase.preferences.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideValidateProfilePictureUseCase(): ValidateProfilePictureUseCase {
        return ValidateProfilePictureUseCaseImpl()
    }

    @Provides
    @Singleton
    fun provideValidateUsernameUseCase(): ValidateUsernameUseCase {
        return ValidateUsernameUseCaseImpl()
    }

    @Provides
    @Singleton
    fun provideEmailValidator(): EmailValidator {
        return AndroidEmailValidator()
    }

    @Provides
    @Singleton
    fun provideValidateEmailUseCase(emailValidator: EmailValidator): ValidateEmailUseCase {
        return ValidateEmailUseCaseImpl(emailValidator)
    }

    @Provides
    @Singleton
    fun provideValidatePasswordUseCase(): ValidatePasswordUseCase {
        return ValidatePasswordUseCaseImpl()
    }

    @Provides
    @Singleton
    fun provideValidateRepeatedPasswordUseCase(): ValidateRepeatedPasswordUseCase {
        return ValidateRepeatedPasswordUseCaseImpl()
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authenticationRepository: AuthenticationRepository): LoginUseCase {
        return LoginUseCaseImpl(authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(authenticationRepository: AuthenticationRepository): SignUpUseCase {
        return SignUpUseCaseImpl(authenticationRepository)
    }

    /**
     * Preferences use cases
     */
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
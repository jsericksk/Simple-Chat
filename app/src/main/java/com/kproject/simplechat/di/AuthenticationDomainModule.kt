package com.kproject.simplechat.di

import com.kproject.simplechat.domain.repository.firebase.AuthenticationRepository
import com.kproject.simplechat.domain.usecase.authentication.LogoutUseCase
import com.kproject.simplechat.domain.usecase.authentication.login.LoginUseCase
import com.kproject.simplechat.domain.usecase.authentication.signup.SignUpUseCase
import com.kproject.simplechat.domain.usecase.authentication.validation.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationDomainModule {

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
        return LoginUseCase(authenticationRepository::login)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(authenticationRepository: AuthenticationRepository): SignUpUseCase {
        return SignUpUseCase(authenticationRepository::signUp)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(authenticationRepository: AuthenticationRepository): LogoutUseCase {
        return LogoutUseCase(authenticationRepository::logout)
    }
}
package com.kproject.simplechat.di

import com.kproject.simplechat.domain.usecase.authentication.validation.ValidateEmailUseCase
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidateEmailUseCaseImpl
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidatePasswordUseCase
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidatePasswordUseCaseImpl
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
    fun provideValidateEmailUseCase(): ValidateEmailUseCase {
        return ValidateEmailUseCaseImpl()
    }

    @Provides
    @Singleton
    fun provideValidatePasswordUseCase(): ValidatePasswordUseCase {
        return ValidatePasswordUseCaseImpl()
    }

}
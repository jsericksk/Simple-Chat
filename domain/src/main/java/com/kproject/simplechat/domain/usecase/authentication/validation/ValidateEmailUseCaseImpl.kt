package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

class ValidateEmailUseCaseImpl : ValidateEmailUseCase {

    override fun invoke(email: String): ValidationState {
        if (email.isBlank()) {
            return ValidationState.EmptyEmail
        }
        return ValidationState.Success
    }
}
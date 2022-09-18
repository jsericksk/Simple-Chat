package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

class ValidatePasswordUseCaseImpl : ValidatePasswordUseCase {

    override fun invoke(password: String): ValidationState {
        if (password.isBlank()) {
            return ValidationState.EmptyPassword
        }

        if (password.any { it.isDigit() }) {
            return ValidationState.InvalidPassword
        }

        return ValidationState.Success
    }
}
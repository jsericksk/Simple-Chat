package com.kproject.simplechat.domain.usecase.firebase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

class ValidatePasswordUseCaseImpl : ValidatePasswordUseCase {

    override fun invoke(password: String): ValidationState {
        if (password.isBlank()) {
            return ValidationState.EmptyPassword
        }

        if (password.length < 6) {
            return ValidationState.PasswordTooShort
        }

        val passwordContainsDigit = password.any { it.isDigit() }
        if (!passwordContainsDigit) {
            return ValidationState.InvalidPassword
        }

        return ValidationState.Success
    }
}
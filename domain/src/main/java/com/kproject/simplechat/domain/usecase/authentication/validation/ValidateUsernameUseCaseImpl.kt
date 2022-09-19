package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

class ValidateUsernameUseCaseImpl : ValidateUsernameUseCase {

    override fun invoke(username: String): ValidationState {
        if (username.isBlank()) {
            return ValidationState.UsernameEmpty
        }

        if (username.startsWith(" ") || username.startsWith("@")) {
            return ValidationState.InvalidUsername
        }

        return ValidationState.Success
    }
}
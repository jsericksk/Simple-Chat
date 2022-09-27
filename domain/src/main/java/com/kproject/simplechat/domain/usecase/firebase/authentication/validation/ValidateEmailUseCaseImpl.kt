package com.kproject.simplechat.domain.usecase.firebase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

class ValidateEmailUseCaseImpl(
    private val emailValidator: EmailValidator
) : ValidateEmailUseCase {

    override fun invoke(email: String): ValidationState {
        if (email.isBlank()) {
            return ValidationState.EmptyEmail
        }

        if (!emailValidator.isValidEmail(email)) {
            return ValidationState.InvalidEmail
        }

        return ValidationState.Success
    }
}
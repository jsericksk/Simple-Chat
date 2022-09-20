package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

class ValidateRepeatedPasswordUseCaseImpl : ValidateRepeatedPasswordUseCase {

    override fun invoke(password: String, repeatedPassword: String): ValidationState {
        if (repeatedPassword != password) {
            return ValidationState.RepeatedPasswordDoesNotMatch
        }

        return ValidationState.Success
    }
}
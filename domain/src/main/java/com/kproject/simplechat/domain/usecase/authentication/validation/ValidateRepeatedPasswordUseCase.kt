package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

interface ValidateRepeatedPasswordUseCase {
    operator fun invoke(password: String, repeatedPassword: String): ValidationState
}
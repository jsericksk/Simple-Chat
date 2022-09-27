package com.kproject.simplechat.domain.usecase.firebase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

interface ValidatePasswordUseCase {
    operator fun invoke(password: String): ValidationState
}
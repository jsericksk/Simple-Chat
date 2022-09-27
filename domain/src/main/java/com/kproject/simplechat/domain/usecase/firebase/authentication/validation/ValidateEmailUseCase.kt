package com.kproject.simplechat.domain.usecase.firebase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

interface ValidateEmailUseCase {
    operator fun invoke(email: String): ValidationState
}
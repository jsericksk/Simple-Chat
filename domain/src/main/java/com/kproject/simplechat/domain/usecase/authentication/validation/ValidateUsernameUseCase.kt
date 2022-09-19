package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.validation.ValidationState

interface ValidateUsernameUseCase {
    operator fun invoke(username: String): ValidationState
}
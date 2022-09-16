package com.kproject.simplechat.domain.usecase.authentication.validation

interface ValidatePasswordUseCase {
    operator fun invoke(password: String): Boolean
}
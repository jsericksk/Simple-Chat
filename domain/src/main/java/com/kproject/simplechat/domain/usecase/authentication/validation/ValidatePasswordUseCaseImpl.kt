package com.kproject.simplechat.domain.usecase.authentication.validation

import com.kproject.simplechat.commom.exception.AuthenticationException

class ValidatePasswordUseCaseImpl : ValidatePasswordUseCase {

    override fun invoke(password: String){
        if (password.isBlank()) {
            throw AuthenticationException.EmptyPasswordException
        }

        if (password.contains(" ")) {
            throw AuthenticationException.InvalidPasswordException
        }
    }
}
package com.kproject.simplechat.domain.usecase.authentication.signup

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.authentication.SignUp
import com.kproject.simplechat.domain.repository.firebase.AuthenticationRepository

class SignUpUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository
) : SignUpUseCase {

    override suspend fun invoke(signUp: SignUp): DataState<Unit> {
        return authenticationRepository.signUp(signUp)
    }
}
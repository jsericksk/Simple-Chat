package com.kproject.simplechat.domain.usecase.authentication.login

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.authentication.Login
import com.kproject.simplechat.domain.repository.authentication.AuthenticationRepository

class LoginUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository
) : LoginUseCase {

    override suspend fun invoke(login: Login): DataState<Nothing> {
        return authenticationRepository.login(login)
    }
}
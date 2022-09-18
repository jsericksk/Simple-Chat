package com.kproject.simplechat.domain.usecase.authentication.login

import com.kproject.simplechat.commom.DataState

class LoginUseCaseImpl(

) : LoginUseCase {

    override suspend fun invoke(email: String, password: String): DataState<Nothing> {
        return DataState.Success()
    }
}
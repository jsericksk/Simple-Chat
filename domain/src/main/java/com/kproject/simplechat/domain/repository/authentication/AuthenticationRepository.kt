package com.kproject.simplechat.domain.repository.authentication

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.authentication.Login
import com.kproject.simplechat.domain.model.authentication.SignUp

interface AuthenticationRepository {

    suspend fun login(login: Login): DataState<Nothing>

    suspend fun signUp(signUp: SignUp): DataState<Nothing>

    suspend fun logout(): DataState<Nothing>
}
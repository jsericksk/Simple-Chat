package com.kproject.simplechat.domain.repository.firebase

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.authentication.Login
import com.kproject.simplechat.domain.model.authentication.SignUp

interface AuthenticationRepository {

    suspend fun login(login: Login): DataState<Unit>

    suspend fun signUp(signUp: SignUp): DataState<Unit>

    suspend fun logout(): DataState<Unit>
}
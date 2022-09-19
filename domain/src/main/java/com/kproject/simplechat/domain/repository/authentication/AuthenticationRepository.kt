package com.kproject.simplechat.domain.repository.authentication

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.authentication.Login
import com.kproject.simplechat.domain.model.authentication.SignUp

interface AuthenticationRepository {

    fun login(login: Login): DataState<Nothing>

    fun signUp(signUp: SignUp): DataState<Nothing>

    fun logout(): DataState<Nothing>
}
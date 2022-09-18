package com.kproject.simplechat.domain.repository.authentication

import com.kproject.simplechat.commom.DataState

interface AuthenticationRepository {

    fun login(email: String, password: String): DataState<Nothing>

    fun signUp(email: String, password: String): DataState<Nothing>
}
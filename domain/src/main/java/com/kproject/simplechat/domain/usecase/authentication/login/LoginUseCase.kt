package com.kproject.simplechat.domain.usecase.authentication.login

import com.kproject.simplechat.commom.DataState

interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): DataState<Nothing>
}
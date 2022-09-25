package com.kproject.simplechat.domain.usecase.authentication.login

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.authentication.Login

fun interface LoginUseCase {
    suspend operator fun invoke(login: Login): DataState<Unit>
}
package com.kproject.simplechat.domain.usecase.firebase.authentication

import com.kproject.simplechat.commom.DataState

fun interface LogoutUseCase: suspend () -> DataState<Unit>
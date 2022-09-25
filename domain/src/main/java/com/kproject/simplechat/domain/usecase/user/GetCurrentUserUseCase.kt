package com.kproject.simplechat.domain.usecase.user

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.UserModel

fun interface GetCurrentUserUseCase : suspend () -> DataState<UserModel>
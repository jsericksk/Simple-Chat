package com.kproject.simplechat.domain.usecase.firebase.user

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.UserModel

fun interface GetCurrentUserUseCase {
    suspend operator fun invoke(): DataState<UserModel>
}
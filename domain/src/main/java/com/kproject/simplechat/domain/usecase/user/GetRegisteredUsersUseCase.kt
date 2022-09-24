package com.kproject.simplechat.domain.usecase.user

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.UserModel
import kotlinx.coroutines.flow.Flow

interface GetRegisteredUsersUseCase {
    suspend operator fun invoke(): Flow<DataState<List<UserModel>>>
}
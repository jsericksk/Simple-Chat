package com.kproject.simplechat.domain.usecase.user

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.UserModel
import com.kproject.simplechat.domain.repository.firebase.UserRepository
import kotlinx.coroutines.flow.Flow

class GetRegisteredUsersUseCaseImpl(
    private val userRepository: UserRepository
) : GetRegisteredUsersUseCase {

    override suspend fun invoke(): Flow<DataState<List<UserModel>>> {
        return userRepository.getRegisteredUsers()
    }
}
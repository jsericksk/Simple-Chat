package com.kproject.simplechat.domain.usecase.user

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.LatestMessageModel
import com.kproject.simplechat.domain.repository.firebase.UserRepository
import kotlinx.coroutines.flow.Flow

class GetLatestMessagesUseCaseImpl(
    private val userRepository: UserRepository
) : GetLatestMessagesUseCase {

    override suspend fun invoke(): Flow<DataState<List<LatestMessageModel>>> {
        return userRepository.getLatestMessages()
    }
}
package com.kproject.simplechat.domain.usecase.user

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.LatestMessageModel
import kotlinx.coroutines.flow.Flow

fun interface GetLatestMessagesUseCase {
    suspend operator fun invoke(): Flow<DataState<List<LatestMessageModel>>>
}
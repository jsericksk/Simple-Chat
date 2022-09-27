package com.kproject.simplechat.domain.usecase.firebase.chat

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.ChatMessageModel
import kotlinx.coroutines.flow.Flow

fun interface GetMessagesUseCase {
    suspend operator fun invoke(fromUserId: String): Flow<DataState<List<ChatMessageModel>>>
}
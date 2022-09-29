package com.kproject.simplechat.domain.repository.firebase

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.ChatMessageModel
import com.kproject.simplechat.domain.model.firebase.UserModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun getMessages(fromUserId: String): Flow<DataState<List<ChatMessageModel>>>

    suspend fun sendMessage(message: ChatMessageModel): DataState<Unit>

    suspend fun saveLatestMessage(
        user: UserModel,
        chatMessage: ChatMessageModel
    ): DataState<Unit>
}
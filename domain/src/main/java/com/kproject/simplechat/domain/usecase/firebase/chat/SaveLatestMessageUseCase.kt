package com.kproject.simplechat.domain.usecase.firebase.chat

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.ChatMessageModel
import com.kproject.simplechat.domain.model.firebase.UserModel

fun interface SaveLatestMessageUseCase {

    suspend operator fun invoke(
        user: UserModel,
        chatMessageModel: ChatMessageModel
    ): DataState<Unit>
}
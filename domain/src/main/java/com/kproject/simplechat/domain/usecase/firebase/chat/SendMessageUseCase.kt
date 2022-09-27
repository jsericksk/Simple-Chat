package com.kproject.simplechat.domain.usecase.firebase.chat

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.ChatMessageModel

fun interface SendMessageUseCase {
    suspend operator fun invoke(message: ChatMessageModel): DataState<Unit>
}
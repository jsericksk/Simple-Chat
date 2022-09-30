package com.kproject.simplechat.domain.usecase.firebase.chat

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.ChatMessageNotificationModel

fun interface PostNotificationUseCase {

    suspend operator fun invoke(
        userId: String,
        chatMessageNotificationModel: ChatMessageNotificationModel
    ): DataState<Unit>
}
package com.kproject.simplechat.domain.repository.firebase

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.ChatMessageNotificationModel

interface PushNotificationRepository {

    suspend fun subscribeToTopic(userId: String): DataState<Unit>

    suspend fun unsubscribeFromTopic(userId: String): DataState<Unit>

    suspend fun postNotification(
        userId: String,
        chatMessageNotificationModel: ChatMessageNotificationModel
    ): DataState<Unit>
}
package com.kproject.simplechat.data.repository.firebase

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.commom.constants.PrefsConstants
import com.kproject.simplechat.data.mapper.toEntity
import com.kproject.simplechat.data.repository.firebase.network.PushNotificationApiService
import com.kproject.simplechat.data.repository.firebase.network.model.PushNotification
import com.kproject.simplechat.domain.model.firebase.ChatMessageNotificationModel
import com.kproject.simplechat.domain.repository.firebase.PushNotificationRepository
import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository
import kotlinx.coroutines.tasks.await

private const val TAG = "PushNotificationRepositoryImpl"

class PushNotificationRepositoryImpl(
    private val pushNotificationApiService: PushNotificationApiService,
    private val dataStoreRepository: DataStoreRepository
) : PushNotificationRepository {

    override suspend fun subscribeToTopic(userId: String): DataState<Unit> {
        return try {
            Firebase.messaging.subscribeToTopic("/topics/$userId").await()
            dataStoreRepository.savePreference(
                key = PrefsConstants.IsSubscribedToReceiveNotifications,
                value = true
            )
            DataState.Success()
        } catch (e: Exception) {
            DataState.Error()
        }
    }

    override suspend fun unsubscribeFromTopic(userId: String): DataState<Unit> {
        return try {
            Firebase.messaging.unsubscribeFromTopic("/topics/$userId").await()
            dataStoreRepository.savePreference(
                key = PrefsConstants.IsSubscribedToReceiveNotifications,
                value = false
            )
            DataState.Success()
        } catch (e: Exception) {
            Log.e(TAG, "Error unsubscribeFromTopic(): ${e.message}")
            DataState.Error()
        }
    }

    override suspend fun postNotification(
        userId: String,
        chatMessageNotificationModel: ChatMessageNotificationModel
    ): DataState<Unit> {
        return try {
            val notification = PushNotification(
                data = chatMessageNotificationModel.toEntity(),
                to = "/topics/$userId"
            )
            pushNotificationApiService.postNotification(notification)
            DataState.Success()
        } catch (e: Exception) {
            Log.e(TAG, "Error postNotification(): ${e.message}")
            DataState.Error()
        }
    }
}
package com.kproject.simplechat.data.repository.firebase

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.commom.constants.PrefsConstants
import com.kproject.simplechat.domain.model.firebase.ChatMessageNotificationModel
import com.kproject.simplechat.domain.repository.firebase.PushNotificationRepository
import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val TAG = "PushNotificationRepositoryImpl"

class PushNotificationRepositoryImpl(
    private val dataStoreRepository: DataStoreRepository
) : PushNotificationRepository {

    override suspend fun subscribeToTopic(userId: String): DataState<Unit> {
        return try {
            Firebase.messaging.subscribeToTopic("/topics/$userId").await()
            CoroutineScope(Dispatchers.IO).launch {
                dataStoreRepository.savePreference(
                    key = PrefsConstants.IsSubscribedToReceiveNotifications,
                    value = true
                )
            }
            DataState.Success()
        } catch (e: Exception) {
            Log.e(TAG, "Error subscribeToTopic(): ${e.message}")
            DataState.Error()
        }
    }


    override suspend fun unsubscribeFromTopic(userId: String): DataState<Unit> {
        return try {
            Firebase.messaging.unsubscribeFromTopic("/topics/$userId").await()
            CoroutineScope(Dispatchers.IO).launch {
                dataStoreRepository.savePreference(
                    key = PrefsConstants.IsSubscribedToReceiveNotifications,
                    value = false
                )
            }
            DataState.Success()
        } catch (e: Exception) {
            Log.e(TAG, "Error unsubscribeFromTopic(): ${e.message}")
            DataState.Error()
        }
    }

    override suspend fun postNotification(chatMessageNotificationModel: ChatMessageNotificationModel): DataState<Unit> {
        TODO("Not yet implemented")
    }
}
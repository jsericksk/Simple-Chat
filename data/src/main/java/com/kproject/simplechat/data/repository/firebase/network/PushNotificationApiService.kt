package com.kproject.simplechat.data.repository.firebase.network

import com.kproject.simplechat.data.repository.firebase.network.model.PushNotification
import com.kproject.simplechat.data.utils.FCMServerKey
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PushNotificationApiService {

    @Headers(
        "Authorization: key=${FCMServerKey.ServerKey}",
        "Content-Type:application/json"
    )
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}
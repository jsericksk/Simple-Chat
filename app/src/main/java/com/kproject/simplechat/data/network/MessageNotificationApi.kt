package com.kproject.simplechat.data.network

import com.kproject.simplechat.data.network.models.PushNotification
import com.kproject.simplechat.utils.Constants
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MessageNotificationApi {

    @Headers(
        "Authorization: key=${Constants.SERVER_KEY}",
        "Content-Type:${Constants.CONTENT_TYPE}"
    )
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}
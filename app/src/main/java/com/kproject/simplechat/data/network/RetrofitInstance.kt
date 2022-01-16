package com.kproject.simplechat.data.network

import com.kproject.simplechat.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val notificationApi: MessageNotificationApi by lazy {
            retrofit.create(MessageNotificationApi::class.java)
        }
    }
}
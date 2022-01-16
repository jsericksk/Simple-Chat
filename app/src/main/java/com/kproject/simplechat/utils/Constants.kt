package com.kproject.simplechat.utils

import com.kproject.simplechat.BuildConfig

object Constants {
    const val COLLECTION_USERS = "users"
    const val COLLECTION_CHAT_MESSAGES = "chat_messages"
    const val COLLECTION_LAST_MESSAGES = "last_messages"
    const val COLLECTION_MESSAGES = "messages"

    const val BASE_URL = "https://fcm.googleapis.com"
    const val SERVER_KEY = BuildConfig.FCM_SERVER_KEY
    const val CONTENT_TYPE = "application/json"
}
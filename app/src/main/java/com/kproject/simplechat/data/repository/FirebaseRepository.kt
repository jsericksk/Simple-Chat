package com.kproject.simplechat.data.repository

import android.net.Uri
import com.kproject.simplechat.data.DataStateResult

interface FirebaseRepository {
    suspend fun signIn(email: String, password: String): DataStateResult

    suspend fun signUp(
        email: String,
        password: String,
        userName: String,
        profileImage: Uri
    ): DataStateResult

    suspend fun logout(): DataStateResult

    suspend fun getLastMessages(): DataStateResult

    suspend fun getAllUsers(): DataStateResult

    suspend fun getMessagesFromUser(userId: String): DataStateResult
}
package com.kproject.simplechat.data.repository

import android.net.Uri
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.model.User

interface FirebaseRepository {
    suspend fun signIn(email: String, password: String): DataStateResult<Unit>

    suspend fun signUp(
        email: String,
        password: String,
        userName: String,
        profileImage: Uri
    ): DataStateResult<Unit>

    suspend fun logout(): DataStateResult<Unit>

    suspend fun getLastMessages(): DataStateResult<Unit>

    suspend fun getRegisteredUserList(): DataStateResult<List<User>>

    suspend fun getMessages(roomId: String): DataStateResult<Unit>
}
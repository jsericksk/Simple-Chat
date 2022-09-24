package com.kproject.simplechat.domain.repository.firebase

import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.domain.model.firebase.LatestMessageModel
import com.kproject.simplechat.domain.model.firebase.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getLatestMessages(): Flow<DataState<List<LatestMessageModel>>>

    suspend fun getRegisteredUsers(): Flow<DataState<List<UserModel>>>
}
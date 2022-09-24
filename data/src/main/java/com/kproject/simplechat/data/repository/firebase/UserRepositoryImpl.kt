package com.kproject.simplechat.data.repository.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.data.mapper.toUserModel
import com.kproject.simplechat.data.model.UserEntity
import com.kproject.simplechat.data.utils.Constants
import com.kproject.simplechat.domain.model.firebase.LatestMessageModel
import com.kproject.simplechat.domain.model.firebase.UserModel
import com.kproject.simplechat.domain.repository.firebase.UserRepository
import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val TAG = "UserRepositoryImpl"

class UserRepositoryImpl(
    private val firebaseFirestore: FirebaseFirestore,
    private val dataStoreRepository: DataStoreRepository
) : UserRepository {

    override suspend fun getLatestMessages(): Flow<DataState<List<LatestMessageModel>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRegisteredUserList() = callbackFlow {
        var snapshotListener: ListenerRegistration? = null
        try {
            val userList = mutableListOf<UserEntity>()

            val docReference = firebaseFirestore.collection(Constants.FirebaseCollectionUsers)
            snapshotListener = docReference.addSnapshotListener { querySnapshot, e ->
                querySnapshot?.let {
                    // Avoid submitting a duplicate list
                    if (userList.isNotEmpty()) {
                        userList.clear()
                    }

                    for (document in it.documents) {
                        document.toObject(UserEntity::class.java)?.let { user ->
                            userList.add(user)
                        }
                    }

                    val registeredUsers = userList.map { userEntity ->
                        userEntity.toUserModel()
                    }
                    trySend(DataState.Success(data = registeredUsers))
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error getRegisteredUserList(): ${e.message}")
            trySend(DataState.Error())
        }

        awaitClose {
            snapshotListener?.remove()
        }
    }
}
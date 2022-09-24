package com.kproject.simplechat.data.repository.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.data.mapper.toLatestMessageModel
import com.kproject.simplechat.data.mapper.toUserModel
import com.kproject.simplechat.data.model.LatestMessageEntity
import com.kproject.simplechat.data.model.UserEntity
import com.kproject.simplechat.data.utils.Constants
import com.kproject.simplechat.domain.repository.firebase.UserRepository
import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

private const val TAG = "UserRepositoryImpl"

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val dataStoreRepository: DataStoreRepository
) : UserRepository {

    override suspend fun getLatestMessages() = callbackFlow {
        var snapshotListener: ListenerRegistration? = null
        try {
            getCurrentUserId()?.let { currentUserId ->
                val docReference = firebaseFirestore
                    .collection(Constants.FirebaseCollectionLatestMessages)
                    .document(currentUserId).collection(Constants.FirebaseCollectionMessages)
                    .orderBy("timestamp", Query.Direction.DESCENDING)

                val latestMessageList = mutableListOf<LatestMessageEntity>()
                snapshotListener = docReference.addSnapshotListener { querySnapshot, e ->
                    querySnapshot?.let {
                        if (latestMessageList.isNotEmpty()) {
                            latestMessageList.clear()
                        }

                        for (document in it.documents) {
                            document.toObject(LatestMessageEntity::class.java)
                                ?.let { latestMessage ->
                                    latestMessageList.add(latestMessage)
                                }
                        }

                        val latestMessages = latestMessageList.map { latestMessageEntity ->
                            latestMessageEntity.toLatestMessageModel()
                        }

                        trySend(DataState.Success(data = latestMessages))
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error: getLatestMessages(): ${e.message}")
            trySend(DataState.Error())
        }
        awaitClose { snapshotListener?.remove() }
    }

    override suspend fun getRegisteredUsers() = callbackFlow {
        var snapshotListener: ListenerRegistration? = null
        try {
            val userList = mutableListOf<UserEntity>()
            val docReference = firebaseFirestore.collection(Constants.FirebaseCollectionUsers)
            snapshotListener = docReference.addSnapshotListener { querySnapshot, e ->
                querySnapshot?.let {
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

    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}
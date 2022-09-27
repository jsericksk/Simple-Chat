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
import com.kproject.simplechat.domain.model.firebase.UserModel
import com.kproject.simplechat.domain.repository.firebase.UserRepository
import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

private const val TAG = "UserRepositoryImpl"

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val dataStoreRepository: DataStoreRepository
) : UserRepository {

    override suspend fun getLatestMessages() = callbackFlow {
        var snapshotListener: ListenerRegistration? = null
        try {
            getLoggedUserId()?.let { loggedUserId ->
                val docReference = firebaseFirestore
                    .collection(Constants.FirebaseCollectionLatestMessages)
                    .document(loggedUserId).collection(Constants.FirebaseCollectionMessages)
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
            val docReference = firebaseFirestore
                .collection(Constants.FirebaseCollectionUsers)
                .orderBy("registrationDate", Query.Direction.DESCENDING)
            snapshotListener = docReference.addSnapshotListener { querySnapshot, e ->
                querySnapshot?.let {
                    if (userList.isNotEmpty()) {
                        userList.clear()
                    }

                    for (document in it.documents) {
                        val loggedUserId = getLoggedUserId()
                        document.toObject(UserEntity::class.java)?.let { user ->
                            if (user.userId != loggedUserId) {
                                userList.add(user)
                            }
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

    override suspend fun getCurrentUser(): DataState<UserModel> {
        return try {
            getLoggedUserId()?.let { userId ->
                val documentSnapshot = firebaseFirestore.collection(Constants.FirebaseCollectionUsers)
                    .document(userId).get().await()
                documentSnapshot?.let { document ->
                    val user = document.toObject(UserEntity::class.java)
                    user?.let { userEntity ->
                        return DataState.Success(userEntity.toUserModel())
                    }
                }
            }

            return DataState.Error()
        } catch (e: Exception) {
            Log.d(TAG, "Error getCurrentUser: ${e.message}")
            DataState.Error()
        }
    }

    override fun getLoggedUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}
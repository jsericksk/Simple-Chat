package com.kproject.simplechat.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.storage.ktx.storage
import com.kproject.simplechat.R
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.model.LastMessage
import com.kproject.simplechat.model.Message
import com.kproject.simplechat.model.User
import com.kproject.simplechat.utils.Constants
import com.kproject.simplechat.utils.DataStoreUtils
import com.kproject.simplechat.utils.PrefsConstants
import com.kproject.simplechat.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

const val TAG = "FirebaseRepositoryImpl"

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
): FirebaseRepository {

    override suspend fun signIn(email: String, password: String): DataStateResult<Unit> {
        var result: DataStateResult<Unit> = DataStateResult.Loading()
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result = DataStateResult.Success()
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> {
                    result = DataStateResult.Error(R.string.error_user_not_found)
                }
                "ERROR_WRONG_PASSWORD" -> {
                    result = DataStateResult.Error(R.string.error_password_is_wrong)
                }
            }
            Log.d(TAG, "Error: ${e.errorCode}")
        } catch (e: Exception) {
            result = DataStateResult.Error(R.string.error_sign_in)
        }
        return result
    }

    override suspend fun signUp(
        email: String,
        password: String,
        userName: String,
        profileImage: Uri
    ): DataStateResult<Unit> {
        var result: DataStateResult<Unit> = DataStateResult.Loading()
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val profileImageUrl = saveProfileImageInFirebaseStorage(profileImage)
            saveUserInFirebaseFirestore(userName, profileImageUrl)
            result = DataStateResult.Success()
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> {
                    result = DataStateResult.Error(R.string.error_email_already_in_use)
                }
            }
            Log.d(TAG, "Error: ${e.errorCode}")
        } catch (e: Exception) {
            result = DataStateResult.Error(R.string.error_sign_up)
        }
        return result
    }

    private suspend fun saveProfileImageInFirebaseStorage(profileImage: Uri): String {
        var profileImageUrl = ""
        try {
            val imageName = UUID.randomUUID().toString()
            profileImageUrl = Firebase.storage.reference.child("profile_images/$imageName")
                .putFile(profileImage).await().storage.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.d(TAG, "Error: saveProfileImageInFirebaseStorage(): ${e.message}")
        }
        return profileImageUrl
    }

    private suspend fun saveUserInFirebaseFirestore(
        userName: String,
        profileImage: String
    ) {
        val userId = firebaseAuth.currentUser?.uid
        userId?.let {
            val user = User(
                userId = userId,
                userName = userName,
                profileImage = profileImage
            )

            firebaseFirestore
                .collection(Constants.COLLECTION_USERS)
                .document(userId)
                .set(user)
                .await()
        }
    }

    override suspend fun logout(): DataStateResult<Unit> {
        var result: DataStateResult<Unit> = DataStateResult.Loading()
        try {
            /**
             * It is necessary to unsubscribe from the topic to not receive notifications
             * from a previously logged in user.
             */
            Firebase.messaging.unsubscribeFromTopic("/topics/${Utils.getCurrentUserId()}")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        CoroutineScope(Dispatchers.IO).launch {
                            DataStoreUtils.savePreference(
                                key = "isSubscribed",
                                value = false
                            )
                        }
                        firebaseAuth.signOut()
                        result = DataStateResult.Success()
                    } else {
                        Log.d(
                            TAG,
                            "Error when trying to unsubscribe from the topic: ${task.exception?.message}"
                        )
                        result = DataStateResult.Error()
                    }
                }.await()
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Error when trying to unsubscribe from the topic: ${e.message}"
            )
            result = DataStateResult.Error()
        }
        return result
    }

    @ExperimentalCoroutinesApi
    override suspend fun getLatestMessages() =
            callbackFlow<DataStateResult<List<LastMessage>>> {
                var snapshotListener: ListenerRegistration? = null
                try {
                    val latestMessageList = mutableListOf<LastMessage>()
                    val docReference = firebaseFirestore
                        .collection(Constants.COLLECTION_LAST_MESSAGES)
                        .document(Utils.getCurrentUserId()).collection(Constants.COLLECTION_MESSAGES)
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                    snapshotListener = docReference.addSnapshotListener { querySnapshot, e ->
                        querySnapshot?.let {
                            if (latestMessageList.isNotEmpty()) {
                                latestMessageList.clear()
                            }

                            for (document in it.documents) {
                                val lastMessage = document.toObject(LastMessage::class.java)
                                latestMessageList.add(lastMessage!!)
                            }

                            trySend(DataStateResult.Success(data = latestMessageList))
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "Error: getLatestMessages(): ${e.message}")
                    trySend(DataStateResult.Error())
                }
                awaitClose { snapshotListener?.remove() }
            }

    @ExperimentalCoroutinesApi
    override suspend fun getRegisteredUserList() =
            callbackFlow<DataStateResult<List<User>>> {
                var snapshotListener: ListenerRegistration? = null
                try {
                    val userList = mutableListOf<User>()
                    var currentUser: User? = null

                    val docReference = firebaseFirestore.collection(Constants.COLLECTION_USERS)
                    snapshotListener = docReference.addSnapshotListener { querySnapshot, e ->
                        querySnapshot?.let {
                            // Avoid submitting a duplicate list
                            if (userList.isNotEmpty()) {
                                userList.clear()
                            }

                            for (document in it.documents) {
                                val user = document.toObject(User::class.java)
                                if (user?.userId != Utils.getCurrentUserId()) {
                                    userList.add(user!!)
                                } else {
                                    currentUser = user
                                }
                            }
                            trySend(DataStateResult.Success(data = userList))
                            /**
                             * Saves important information of the currently logged in user.
                             */
                            CoroutineScope(Dispatchers.IO).launch {
                                saveCurrentUserInformationLocally(currentUser)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "Error: getRegisteredUserList(): ${e.message}")
                    trySend(DataStateResult.Error())
                }
                awaitClose { snapshotListener?.remove() }
            }

    @ExperimentalCoroutinesApi
    override suspend fun getMessages(fromUserId: String) =
            callbackFlow<DataStateResult<List<Message>>> {
                var snapshotListener: ListenerRegistration? = null
                try {
                    val messageList = mutableListOf<Message>()
                    val myUserId = firebaseAuth.currentUser?.uid!!
                    val chatRoomId = Utils.createChatRoomId(fromUserId, myUserId)
                    val docReference = firebaseFirestore
                        .collection(Constants.COLLECTION_CHAT_MESSAGES)
                        .document(chatRoomId).collection(Constants.COLLECTION_MESSAGES)
                        .orderBy("timestamp", Query.Direction.ASCENDING)
                    snapshotListener = docReference.addSnapshotListener { querySnapshot, e ->
                        querySnapshot?.let {
                            // Avoid submitting a duplicate list
                            if (messageList.isNotEmpty()) {
                                messageList.clear()
                            }

                            for (document in it.documents) {
                                val message = document.toObject(Message::class.java)
                                messageList.add(message!!)
                            }

                            trySend(DataStateResult.Success(data = messageList))
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "Error: getMessages(): ${e.message}")
                    trySend(DataStateResult.Error())
                }
                awaitClose { snapshotListener?.remove() }
            }

    override suspend fun sendMessage(
        message: String,
        senderId: String,
        receiverId: String
    ): DataStateResult<Unit> {
        return try {
            val userId = firebaseAuth.currentUser?.uid
            userId?.let {
                val messageObj = Message(
                    message = message,
                    senderId = senderId,
                    receiverId = receiverId
                )
                val chatRoomId = Utils.createChatRoomId(senderId, receiverId)

                firebaseFirestore
                    .collection(Constants.COLLECTION_CHAT_MESSAGES)
                    .document(chatRoomId)
                    .collection(Constants.COLLECTION_MESSAGES)
                    .add(messageObj)
                    .await()
            }
            DataStateResult.Success()
        } catch (e: Exception) {
            Log.d(TAG, "Error: sendMessage(): ${e.message}")
            DataStateResult.Error()
        }
    }

    override suspend fun saveLastMessage(
        lastMessage: String,
        senderId: String,
        receiverId: String,
        userName: String,
        userProfileImage: String
    ): DataStateResult<Unit> {
        return try {
            val currentUser = getLoggedUser()
            currentUser?.let { currentLoggedUser ->
                // receiverId represents the id of the second user in the conversation
                val lastMessageOfCurrentUser = LastMessage(
                    chatId = receiverId,
                    lastMessage = lastMessage,
                    senderId = senderId,
                    receiverId = receiverId,
                    userName = userName,
                    userProfileImage = userProfileImage
                )

                val lastMessageOfSecondUser = LastMessage(
                    chatId = currentLoggedUser.userId,
                    lastMessage = lastMessage,
                    senderId = senderId,
                    receiverId = receiverId,
                    userName = currentLoggedUser.userName,
                    userProfileImage = currentLoggedUser.profileImage
                )

                /**
                 * Save both the last message in each document which is
                 * represented by the user id.
                 */
                firebaseFirestore
                    .collection(Constants.COLLECTION_LAST_MESSAGES)
                    .document(currentLoggedUser.userId)
                    .collection(Constants.COLLECTION_MESSAGES)
                    .document(receiverId)
                    .set(lastMessageOfCurrentUser)
                    .await()

                firebaseFirestore
                    .collection(Constants.COLLECTION_LAST_MESSAGES)
                    .document(receiverId)
                    .collection(Constants.COLLECTION_MESSAGES)
                    .document(currentLoggedUser.userId)
                    .set(lastMessageOfSecondUser)
                    .await()
            }
            DataStateResult.Success()
        } catch (e: Exception) {
            Log.d(TAG, "Error: saveLastMessage(): ${e.message}")
            DataStateResult.Error()
        }
    }

    private suspend fun getLoggedUser(): User? {
        return try {
            var user: User? = null
            val documentSnapshot = firebaseFirestore.collection(Constants.COLLECTION_USERS)
                .document(Utils.getCurrentUserId()).get().await()
            documentSnapshot?.let {
                user = it.toObject(User::class.java)
            }
            user
        } catch (e: Exception) {
            Log.d(TAG, "Error: getLoggedUser(): ${e.message}")
            return null
        }
    }

    private suspend fun saveCurrentUserInformationLocally(currentUser: User?) {
        currentUser?.let { user ->
            DataStoreUtils.savePreference(
                key = PrefsConstants.MY_USER_NAME,
                value = user.userName
            )
            DataStoreUtils.savePreference(
                key = PrefsConstants.MY_USER_PROFILE_IMAGE,
                value = user.profileImage
            )
        }
    }
}
package com.kproject.simplechat.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kproject.simplechat.R
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.model.LastMessage
import com.kproject.simplechat.model.Message
import com.kproject.simplechat.model.User
import com.kproject.simplechat.utils.Utils
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

const val TAG = "FirebaseRepositoryImpl"

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : FirebaseRepository {

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
        return try {
            val imageName = UUID.randomUUID().toString()
            Firebase.storage.reference.child("profile_images/$imageName")
                .putFile(profileImage).await().storage.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.d(TAG, "Error:saveProfileImageInFirebaseStorage(): ${e.message}")
            ""
        }
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
                .collection("users")
                .document(userId)
                .set(user)
                .await()
        }
    }

    override suspend fun logout(): DataStateResult<Unit> {
        return try {
            firebaseAuth.signOut()
            DataStateResult.Success()
        } catch (e: Exception) {
            DataStateResult.Error(R.string.error_logout)
        }
    }

    override suspend fun getLatestMessages(): DataStateResult<List<LastMessage>> {
        return try {
            val myUserId = firebaseAuth.currentUser?.uid!!
            val latestMessageList = mutableListOf<LastMessage>()
            firebaseFirestore.collection("latest_messages").get()
                .addOnSuccessListener { documentSnapshot ->
                    for (doc in documentSnapshot) {
                        val lastMessage = doc.toObject(LastMessage::class.java)
                        if (lastMessage.chatId.contains(myUserId)) {
                            latestMessageList.add(lastMessage)
                        }
                    }
                }.await()
            DataStateResult.Success(latestMessageList)
        } catch (e: Exception) {
            Log.d(TAG, "Error:getRegisteredUserList(): ${e.message}")
            DataStateResult.Error(R.string.error_get_user_list)
        }
    }


    override suspend fun getRegisteredUserList(): DataStateResult<List<User>> {
        return try {
            val userList = mutableListOf<User>()
            firebaseFirestore.collection("users").get()
                .addOnSuccessListener { documentSnapshot ->
                    for (doc in documentSnapshot) {
                        val user = doc.toObject(User::class.java)
                        if (user.userId != firebaseAuth.currentUser?.uid) {
                            userList.add(user)
                        }
                    }
                }.await()
            DataStateResult.Success(userList)
        } catch (e: Exception) {
            Log.d(TAG, "Error:getRegisteredUserList(): ${e.message}")
            DataStateResult.Error(R.string.error_get_user_list)
        }
    }

    override suspend fun sendMessage(
        message: String,
        senderId: String,
        receiverId: String
    ): DataStateResult<Unit> {
        return try {
            val userId = firebaseAuth.currentUser?.uid
            userId?.let {
                val message = Message(
                    message = message,
                    senderId = senderId,
                    receiverId = receiverId,
                    timestamp = System.currentTimeMillis()
                )
                val chatRoomId = Utils.createChatRoomId(senderId, receiverId)

                firebaseFirestore
                    .collection("chat_messages")
                    .document(chatRoomId)
                    .collection("messages")
                    .add(message)
                    .await()
            }
            DataStateResult.Success()
        } catch (e: Exception) {
            Log.d(TAG, "Error: sendMessage(): ${e.message}")
            DataStateResult.Error()
        }
    }

    override suspend fun getMessages(fromUserId: String): DataStateResult<List<Message>> {
        return try {
            val messageList = mutableListOf<Message>()
            val myUserId = firebaseAuth.currentUser?.uid!!
            val chatRoomId = Utils.createChatRoomId(fromUserId, myUserId)
            val docReference = firebaseFirestore
                .collection("chat_messages")
                .document(chatRoomId).collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
            docReference.addSnapshotListener { querySnapshot, e ->
                querySnapshot?.let {
                    for (document in it.documents) {
                        val message = document.toObject(Message::class.java)
                        //val message Utils.createChatRoomId()
                        messageList.add(message!!)
                    }
                }
            }
            DataStateResult.Success(messageList)
        } catch (e: Exception) {
            Log.d(TAG, "Error: getMessages(): ${e.message}")
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
            val chatRoomId = Utils.createChatRoomId(senderId, receiverId)
            val userId = firebaseAuth.currentUser?.uid
            userId?.let {
                val chat = LastMessage(
                    chatId = chatRoomId,
                    lastMessage = lastMessage,
                    senderId = senderId,
                    userName = userName,
                    userProfileImage = userProfileImage,
                    timestamp = System.currentTimeMillis()
                )

                firebaseFirestore
                    .collection("latest_messages")
                    .document(chatRoomId)
                    .set(chat)
                    .await()
            }
            DataStateResult.Success()
        } catch (e: Exception) {
            Log.d(TAG, "Error: saveLastMessage(): ${e.message}")
            DataStateResult.Error()
        }
    }

}
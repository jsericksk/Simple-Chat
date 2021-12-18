package com.kproject.simplechat.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kproject.simplechat.R
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.model.Message
import com.kproject.simplechat.model.User
import com.kproject.simplechat.utils.Utils
import kotlinx.coroutines.awaitAll
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
        TODO("Not yet implemented")
    }

    override suspend fun getLastMessages(): DataStateResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getRegisteredUserList(): DataStateResult<List<User>> {
        return try {
            var userList: List<User>? = null
            firebaseFirestore.collection("users").get()
                .addOnSuccessListener { documentSnapshot ->
                    userList = documentSnapshot.toObjects()
                    // Log.d(TAG, "Success:getRegisteredUserList() ${userList!![0]}")
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
                    .collection("chats")
                    .document(chatRoomId)
                    .set(message)
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
            var messageList: List<Message>? = null
            val myUserId = firebaseAuth.currentUser?.uid!!
            val chatRoomId = Utils.createChatRoomId(fromUserId, myUserId)
            val docReference = firebaseFirestore.collection("chats").document(chatRoomId)
            docReference.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    messageList = snapshot.toObject()
                    Log.d(TAG, "Current data: ${snapshot.data}")
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
            DataStateResult.Success(messageList)
        } catch (e: Exception) {
            Log.d(TAG, "Error: getMessages(): ${e.message}")
            DataStateResult.Error()
        }
    }

}
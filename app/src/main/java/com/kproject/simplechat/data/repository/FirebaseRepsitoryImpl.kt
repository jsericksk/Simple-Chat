package com.kproject.simplechat.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kproject.simplechat.R
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.model.User
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*
import javax.inject.Inject

const val TAG = "FirebaseRepositoryImpl"

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    val firebaseFirestore: FirebaseFirestore
): FirebaseRepository {

    override suspend fun signIn(email: String, password: String): DataStateResult {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            DataStateResult.Success(firebaseAuth.currentUser)
        } catch (e: Exception) {
            Log.d(TAG, "Error: ${e.message}")
            DataStateResult.Error()
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        userName: String,
        profileImage: Uri
    ): DataStateResult {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val profileImageUrl = saveProfileImageInFirebaseStorage(profileImage)
            saveUserInFirebaseFirestore(userName, profileImageUrl)
            DataStateResult.Success()
        } catch (e: FirebaseAuthException) {
            Log.d(TAG, "Error: ${e.message}")
            DataStateResult.Error(R.string.button_cancel)
        }
    }

    private suspend fun saveProfileImageInFirebaseStorage(profileImage: Uri): String {
        return try {
            val imageName = UUID.randomUUID().toString()
            Firebase.storage.reference.child("profile_images/$imageName")
                .putFile(profileImage).await().storage.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.d(TAG, "Error: ${e.message}")
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

    override suspend fun logout(): DataStateResult {
        TODO("Not yet implemented")
    }

    override suspend fun getLastMessages(): DataStateResult {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): DataStateResult {
        TODO("Not yet implemented")
    }

    override suspend fun getMessagesFromUser(userId: String): DataStateResult {
        TODO("Not yet implemented")
    }

}
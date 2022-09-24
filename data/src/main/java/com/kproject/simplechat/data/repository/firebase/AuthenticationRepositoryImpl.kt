package com.kproject.simplechat.data.repository.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.commom.constants.PrefsConstants
import com.kproject.simplechat.commom.exception.AuthenticationException
import com.kproject.simplechat.data.model.UserEntity
import com.kproject.simplechat.data.utils.Constants
import com.kproject.simplechat.domain.model.authentication.Login
import com.kproject.simplechat.domain.model.authentication.SignUp
import com.kproject.simplechat.domain.repository.firebase.AuthenticationRepository
import com.kproject.simplechat.domain.repository.preferences.DataStoreRepository
import kotlinx.coroutines.tasks.await
import java.util.*

private const val TAG = "AuthenticationRepositoryImpl"

class AuthenticationRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val dataStoreRepository: DataStoreRepository
) : AuthenticationRepository {

    override suspend fun login(login: Login): DataState<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(login.email, login.password).await()
            saveIsUserLoggIn()
            DataState.Success()
        } catch (e: FirebaseAuthException) {
            Log.e(TAG, "Login error: ${e.message} ${e.errorCode}")
            when (e.errorCode) {
                Constants.ErrorUserNotFound -> {
                    DataState.Error(AuthenticationException.UserNotFoundException)
                }
                Constants.ErrorWrongPassword -> {
                    DataState.Error(AuthenticationException.WrongPasswordException)
                }
                else ->{
                    DataState.Error(AuthenticationException.UnknownLoginException)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Login error: ${e.message}")
            DataState.Error(AuthenticationException.UnknownLoginException)
        }
    }

    override suspend fun signUp(signUp: SignUp): DataState<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(signUp.email, signUp.password).await()
            val profilePictureUrl = saveProfilePictureInStorage(signUp.profileImage)

            saveUserInFirestore(
                username = signUp.username,
                profilePicture = profilePictureUrl
            )

            DataState.Success()
        } catch (e: FirebaseAuthException) {
            Log.d(TAG, "SignUp error: ${e.errorCode}")
            when (e.errorCode) {
                Constants.ErrorEmailAlreadyInUse -> {
                    DataState.Error(AuthenticationException.EmailInUseException)
                }
                else -> {
                    DataState.Error(AuthenticationException.UnknownSignUpException)
                }
            }
        } catch (e: Exception) {
            DataState.Error(AuthenticationException.UnknownSignUpException)
        }
    }

    private suspend fun saveProfilePictureInStorage(profilePicture: String): String {
        return try {
            val imageName = UUID.randomUUID().toString()
            val imageUri = Uri.parse(profilePicture)
            val profilePictureUrl = Firebase.storage.reference.child("profile_images/$imageName")
                .putFile(imageUri).await().storage.downloadUrl.await().toString()

            if (profilePicture.isNotEmpty()) {
                saveIsUserLoggIn()
                dataStoreRepository.savePreference(
                    key = PrefsConstants.UserProfilePicture,
                    value = profilePictureUrl
                )
            }

            profilePictureUrl
        } catch (e: Exception) {
            Log.d(TAG, "saveProfileImageInStorage() error: ${e.message}")
            ""
        }
    }

    private suspend fun saveIsUserLoggIn() {
        dataStoreRepository.savePreference(
            key = PrefsConstants.IsUserLoggedIn,
            value = true
        )
    }

    private suspend fun saveUserInFirestore(username: String, profilePicture: String) {
        val userId = firebaseAuth.currentUser?.uid
        userId?.let { id ->
            val user = UserEntity(
                userId = id,
                username = username,
                profilePicture = profilePicture
            )
            firebaseFirestore
                .collection(Constants.FirebaseCollectionUsers)
                .document(id)
                .set(user)
                .await()
        }
    }

    override suspend fun logout(): DataState<Unit> {
        TODO("Not yet implemented")
    }
}
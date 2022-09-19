package com.kproject.simplechat.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.commom.exception.AuthenticationException
import com.kproject.simplechat.domain.model.authentication.Login
import com.kproject.simplechat.domain.model.authentication.SignUp
import com.kproject.simplechat.domain.repository.authentication.AuthenticationRepository
import kotlinx.coroutines.tasks.await

private const val TAG = "AuthenticationRepositoryImpl"
private const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
private const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"

class AuthenticationRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
) : AuthenticationRepository {

    override suspend fun login(login: Login): DataState<Nothing> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(login.email, login.password).await()
            DataState.Success()
        } catch (e: FirebaseAuthException) {
            Log.e(TAG, "Error in login: ${e.message} ${e.errorCode}")
            when (e.errorCode) {
                ERROR_USER_NOT_FOUND -> {
                    DataState.Error(AuthenticationException.UserNotFoundException)
                }
                ERROR_WRONG_PASSWORD -> {
                    DataState.Error(AuthenticationException.WrongPasswordException)
                }
                else ->{
                    DataState.Error(AuthenticationException.UnknownLoginException)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in login: ${e.message}")
            DataState.Error(AuthenticationException.UnknownLoginException)
        }
    }

    override suspend fun signUp(signUp: SignUp): DataState<Nothing> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): DataState<Nothing> {
        TODO("Not yet implemented")
    }
}
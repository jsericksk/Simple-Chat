package com.kproject.simplechat.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.data.repository.FirebaseRepository
import com.kproject.simplechat.model.LastMessage
import com.kproject.simplechat.model.User
import com.kproject.simplechat.utils.DataStoreUtils
import com.kproject.simplechat.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    private val _latestMessageListState = MutableLiveData<DataStateResult<List<LastMessage>>>()
    val latestMessageListState = _latestMessageListState

    private val _registeredUsersListState = MutableLiveData<DataStateResult<List<User>>>()
    val registeredUsersListState: MutableLiveData<DataStateResult<List<User>>> =
            _registeredUsersListState

    private val _logoutState = MutableLiveData<DataStateResult<Unit>>()
    val logoutState = _logoutState

    private val _loadingLogout = MutableLiveData<Boolean>()
    val loadingLogout = _loadingLogout

    fun logout() {
        _loadingLogout.postValue(true)
        logoutState.postValue(DataStateResult.Loading())
        viewModelScope.launch {
            when (firebaseRepository.logout()) {
                is DataStateResult.Success -> {
                    _logoutState.postValue(DataStateResult.Success())
                }
                is DataStateResult.Error -> {
                    _logoutState.postValue(DataStateResult.Error())
                    _loadingLogout.postValue(false)
                }
                else -> {}
            }
        }
    }

    fun getLatestMessages() {
        viewModelScope.launch {
            _latestMessageListState.postValue(DataStateResult.Loading())
            firebaseRepository.getLatestMessages().collect { result ->
                when (result) {
                    is DataStateResult.Success -> {
                        _latestMessageListState.postValue(result)
                    }
                    is DataStateResult.Error -> {
                        _latestMessageListState.postValue(DataStateResult.Error())
                    }
                    else -> {}
                }
            }
        }
    }

    fun getRegisteredUserList() {
        viewModelScope.launch {
            _registeredUsersListState.postValue(DataStateResult.Loading())
            firebaseRepository.getRegisteredUserList().collect { result ->
                when (result) {
                    is DataStateResult.Success -> {
                        _registeredUsersListState.postValue(result)
                    }
                    is DataStateResult.Error -> {
                        _registeredUsersListState.postValue(DataStateResult.Error())
                    }
                    else -> {}
                }
            }
        }
    }

    /**
     * Subscribes to the topic that is created based on the current user id,
     * to receive notifications of new messages.
     */
    fun subscribeToTopic() {
        val isSubscribed = DataStoreUtils.readPreferenceWithoutFlow(
            key = "isSubscribed",
            defaultValue = false
        )

        if (!isSubscribed) {
            Firebase.messaging.subscribeToTopic("/topics/${Utils.getCurrentUserId()}")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        viewModelScope.launch {
                            DataStoreUtils.savePreference(
                                key = "isSubscribed",
                                value = true
                            )
                        }
                    } else {
                        Log.d(
                            "HomeViewModel",
                            "Error trying to subscribe to the topic: ${task.exception?.message}"
                        )
                    }
                }
        }
    }
}
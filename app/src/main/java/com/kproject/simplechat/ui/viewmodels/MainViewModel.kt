package com.kproject.simplechat.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.data.repository.FirebaseRepository
import com.kproject.simplechat.data.repository.TAG
import com.kproject.simplechat.model.LastMessage
import com.kproject.simplechat.model.Message
import com.kproject.simplechat.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    private val _dataStateResult = MutableLiveData<DataStateResult<Any>>()
    val dataStateResult: MutableLiveData<DataStateResult<Any>> = _dataStateResult

    private val _logout = MutableLiveData<Boolean>()
    val logout: MutableLiveData<Boolean> = _logout

    private val _errorMessageResId = MutableLiveData<Int>()
    val errorMessageResId: MutableLiveData<Int> = _errorMessageResId

    private val _latestMessageList = MutableLiveData<List<LastMessage>>()
    val latestMessageList: MutableLiveData<List<LastMessage>> = _latestMessageList

    private val _registeredUsersList = MutableLiveData<List<User>>()
    val registeredUsersList: MutableLiveData<List<User>> = _registeredUsersList

    private val _messageList = MutableLiveData<List<Message>?>()
    val messageList: MutableLiveData<List<Message>?> = _messageList

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _dataStateResult.postValue(DataStateResult.Loading())
            val data = firebaseRepository.signIn(email = email, password = password)
            when (data) {
                is DataStateResult.Success -> _dataStateResult.postValue(DataStateResult.Success())
                is DataStateResult.Error ->  {
                    _errorMessageResId.postValue(data.errorMessageResId)
                    _dataStateResult.postValue(DataStateResult.Error())
                }
                else -> {}
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        userName: String,
        profileImage: Uri
    ) {
        viewModelScope.launch {
            _dataStateResult.postValue(DataStateResult.Loading())
            val data = firebaseRepository.signUp(
                email = email, password = password,
                userName = userName, profileImage = profileImage
            )
            when (data) {
                is DataStateResult.Success -> _dataStateResult.postValue(DataStateResult.Success())
                is DataStateResult.Error -> {
                    _errorMessageResId.postValue(data.errorMessageResId)
                    _dataStateResult.postValue(DataStateResult.Error())
                }
                else -> {}
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _dataStateResult.postValue(DataStateResult.Loading())
            when (val data = firebaseRepository.logout()) {
                is DataStateResult.Success -> {
                    _logout.postValue(true)
                }
                is DataStateResult.Error -> {
                    _errorMessageResId.postValue(data.errorMessageResId)
                }
                else -> {}
            }
        }
    }

    fun getLatestMessages() {
        Log.d(TAG, "ViewModel getLatestMessages()")
        viewModelScope.launch {
            _dataStateResult.postValue(DataStateResult.Loading())
            when (val data = firebaseRepository.getLatestMessages()) {
                is DataStateResult.Success -> {
                    _latestMessageList.postValue(data.data!!)
                    _dataStateResult.postValue(DataStateResult.Success())
                }
                is DataStateResult.Error ->  {
                    _errorMessageResId.postValue(data.errorMessageResId)
                    _dataStateResult.postValue(DataStateResult.Error())
                }
                else -> {}
            }
        }
    }

    fun getRegisteredUserList() {
        Log.d(TAG, "ViewModel getRegisteredUserList()")
        viewModelScope.launch {
            _dataStateResult.postValue(DataStateResult.Loading())
            when (val data = firebaseRepository.getRegisteredUserList()) {
                is DataStateResult.Success -> {
                    _registeredUsersList.postValue(data.data!!)
                    _dataStateResult.postValue(DataStateResult.Success())
                }
                is DataStateResult.Error ->  {
                    _errorMessageResId.postValue(data.errorMessageResId)
                    _dataStateResult.postValue(DataStateResult.Error())
                }
                else -> {}
            }
        }
    }

    fun sendMessage(message: String, senderId: String, receiverId: String) {
        viewModelScope.launch {
            _dataStateResult.postValue(DataStateResult.Loading())
            when (firebaseRepository.sendMessage(message, senderId, receiverId)) {
                is DataStateResult.Success -> {
                    _dataStateResult.postValue(DataStateResult.Success())
                }
                is DataStateResult.Error ->  {
                    // _errorMessageResId.postValue(data.errorMessageResId)
                    _dataStateResult.postValue(DataStateResult.Error())
                }
                else -> {}
            }
        }
    }

    fun getMessages(fromUserId: String) {
        viewModelScope.launch {
            _dataStateResult.postValue(DataStateResult.Loading())
            when (val data = firebaseRepository.getMessages(fromUserId)) {
                is DataStateResult.Success -> {
                    _messageList.postValue(data.data)
                    _dataStateResult.postValue(DataStateResult.Success())
                }
                is DataStateResult.Error ->  {
                    //_errorMessageResId.postValue(data.errorMessageResId)
                    _dataStateResult.postValue(DataStateResult.Error())
                }
                else -> {}
            }
        }
    }

    fun saveLastMessage(
        lastMessage: String,
        senderId: String,
        receiverId: String,
        userName: String,
        userProfileImage: String
    ) {
        viewModelScope.launch {
            _dataStateResult.postValue(DataStateResult.Loading())
            val data = firebaseRepository.saveLastMessage(
                lastMessage = lastMessage,
                senderId = senderId,
                receiverId = receiverId,
                userName = userName,
                userProfileImage = userProfileImage
            )
            when (data) {
                is DataStateResult.Success -> {
                    _dataStateResult.postValue(DataStateResult.Success())
                }
                is DataStateResult.Error ->  {
                    // _errorMessageResId.postValue(data.errorMessageResId)
                    _dataStateResult.postValue(DataStateResult.Error())
                }
                else -> {}
            }
        }
    }

}
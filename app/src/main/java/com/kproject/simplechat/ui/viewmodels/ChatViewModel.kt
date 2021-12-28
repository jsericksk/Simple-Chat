package com.kproject.simplechat.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.data.repository.FirebaseRepository
import com.kproject.simplechat.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    private val _dataStateResult = MutableLiveData<DataStateResult<Any>>()
    val dataStateResult: MutableLiveData<DataStateResult<Any>> = _dataStateResult

    private val _messageList = MutableLiveData<List<Message>>()
    val messageList: MutableLiveData<List<Message>> = _messageList

    fun sendMessage(message: String, senderId: String, receiverId: String) {
        viewModelScope.launch {
            _dataStateResult.postValue(DataStateResult.Loading())
            when (firebaseRepository.sendMessage(message, senderId, receiverId)) {
                is DataStateResult.Success -> {
                    _dataStateResult.postValue(DataStateResult.Success())
                }
                is DataStateResult.Error ->  {
                    _dataStateResult.postValue(DataStateResult.Error())
                }
                else -> {}
            }
        }
    }

    fun getMessages(fromUserId: String) {
        viewModelScope.launch {
            _dataStateResult.postValue(DataStateResult.Loading())
            firebaseRepository.getMessages(fromUserId).collect { result ->
                when (result) {
                    is DataStateResult.Success -> {
                        val newList = result.data?.toMutableList()!!
                        _messageList.postValue(newList)
                        _dataStateResult.postValue(DataStateResult.Success())
                    }
                    is DataStateResult.Error -> {
                        _dataStateResult.postValue(DataStateResult.Error())
                    }
                    else -> {}
                }
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
                    _dataStateResult.postValue(DataStateResult.Error())
                }
                else -> {}
            }
        }
    }
}
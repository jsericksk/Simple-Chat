package com.kproject.simplechat.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.data.repository.FirebaseRepository
import com.kproject.simplechat.data.repository.TAG
import com.kproject.simplechat.model.LastMessage
import com.kproject.simplechat.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    private val _latestMessageListState = MutableLiveData<DataStateResult<List<LastMessage>>>()
    val latestMessageListState: MutableLiveData<DataStateResult<List<LastMessage>>> = _latestMessageListState

    private val _registeredUsersListState = MutableLiveData<DataStateResult<List<User>>>()
    val registeredUsersListState: MutableLiveData<DataStateResult<List<User>>> = _registeredUsersListState

    private val _logout = MutableLiveData<Boolean>()
    val logout: MutableLiveData<Boolean> = _logout

    fun logout() {
        viewModelScope.launch {
            firebaseRepository.logout()
            _logout.postValue(true)
        }
    }

    /**
     * Mudanças a serem feitas: tornar esse método realtime
     * Criar um id para cada conversa
     */
    fun getLatestMessages() {
        Log.d(TAG, "ViewModel getLatestMessages()")
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
        Log.d(TAG, "ViewModel getRegisteredUserList()")
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
}
package com.kproject.simplechat.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.data.DataStateResult
import com.kproject.simplechat.data.repository.FirebaseRepository
import com.kproject.simplechat.data.repository.TAG
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

    private val _errorMessageResId = MutableLiveData<Int>()
    val errorMessageResId: MutableLiveData<Int> = _errorMessageResId

    private val _registeredUsersList = MutableLiveData<List<User>>()
    val registeredUsersList: MutableLiveData<List<User>> = _registeredUsersList

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _dataStateResult.postValue(DataStateResult.Loading())
            val data = firebaseRepository.signIn(email = email, password = password)
            when (data) {
                is DataStateResult.Success -> dataStateResult.postValue(DataStateResult.Success())
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
                is DataStateResult.Success -> dataStateResult.postValue(DataStateResult.Success())
                is DataStateResult.Error -> {
                    _errorMessageResId.postValue(data.errorMessageResId)
                    _dataStateResult.postValue(DataStateResult.Error())
                }
                else -> {}
            }
        }
    }

    fun logout() {

    }

    fun getLastMessages() {

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

    fun getMessages(userId: String) {

    }


}
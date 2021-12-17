package com.kproject.simplechat.data

sealed class DataStateResult<out T> {
    data class Loading<T>(val loading: Unit? = null) : DataStateResult<T>()
    data class Success<T>(val data: T? = null) : DataStateResult<T>()
    data class Error<T>(val errorMessageResId: Int = 0) : DataStateResult<T>()
}
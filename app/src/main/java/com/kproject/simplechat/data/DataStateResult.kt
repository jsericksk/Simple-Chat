package com.kproject.simplechat.data

sealed class DataStateResult<T>(val data: T? = null) {
    class Loading<T> : DataStateResult<T>()
    class Success<T>(data: T? = null) : DataStateResult<T>(data)
    class Error<T>(val errorMessageResId: Int = 0) : DataStateResult<T>()
}
package com.kproject.simplechat.data

import java.lang.Exception

sealed class DataStateResult {
    object Loading : DataStateResult()
    data class Success(val data: Any? = null) : DataStateResult()
    data class Error(val errorMessageResId: Int = 0) : DataStateResult()
}
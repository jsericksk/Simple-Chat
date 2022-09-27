package com.kproject.simplechat.presentation.mapper

import android.net.Uri
import com.google.gson.Gson

/**
 * Used to convert data class into Json and be able to transfer the data between navigation.
 */
fun <T> String.fromJson(type: Class<T>): T {
    return Gson().fromJson(this, type)
}

fun <T> T.toJson(): String {
    return Uri.encode(Gson().toJson(this))
}
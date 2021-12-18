package com.kproject.simplechat.utils

import android.content.Context
import android.widget.Toast

object Utils {

    fun showToast(context: Context, stringResId: Int) {
        Toast.makeText(context, stringResId, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun createChatRoomId(senderId: String, receiverId: String): String {
        return if (senderId.compareTo(receiverId) <= -1) {
            receiverId + senderId
        } else {
            senderId + receiverId
        }
    }
}
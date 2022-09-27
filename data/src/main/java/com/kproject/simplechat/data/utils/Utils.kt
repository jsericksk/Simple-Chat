package com.kproject.simplechat.data.utils

object Utils {

    fun createChatRoomId(senderId: String, receiverId: String): String {
        return if (senderId.compareTo(receiverId) <= -1) {
            receiverId + senderId
        } else {
            senderId + receiverId
        }
    }
}
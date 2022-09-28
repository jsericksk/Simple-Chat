package com.kproject.simplechat.data.utils

object Utils {

    fun createChatRoomId(userId1: String, userId2: String): String {
        return if (userId1.compareTo(userId2) <= -1) {
            userId2 + userId1
        } else {
            userId1 + userId2
        }
    }
}
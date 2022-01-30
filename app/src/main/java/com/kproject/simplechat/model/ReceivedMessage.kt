package com.kproject.simplechat.model

import java.io.Serializable

data class ReceivedMessage(
    var fromUserId: String,
    var userName: String,
    var userProfileImage: String
) : Serializable
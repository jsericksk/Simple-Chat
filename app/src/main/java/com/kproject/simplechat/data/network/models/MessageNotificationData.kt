package com.kproject.simplechat.data.network.models

data class MessageNotificationData(
    var title: String,
    var message: String,
    var fromUserId: String,
    var fromUserName: String,
    var userProfileImage: String
)
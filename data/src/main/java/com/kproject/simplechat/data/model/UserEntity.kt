package com.kproject.simplechat.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class UserEntity(
    val userId: String = "",
    val username: String = "",
    var profilePicture: String = "",
    @ServerTimestamp
    var registrationDate: Date? = null
)
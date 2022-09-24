package com.kproject.simplechat.domain.model.firebase

import java.util.*

data class UserModel(
    val userId: String = "",
    val username: String = "",
    var profilePicture: String = "",
    var registrationDate: Date? = null
)
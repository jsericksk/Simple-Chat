package com.kproject.simplechat.presentation.model

import android.net.Uri
import com.google.gson.Gson
import com.kproject.simplechat.presentation.utils.Utils
import java.util.*

data class User(
    val userId: String = "",
    val username: String = "",
    var profilePicture: String = "",
    var registrationDate: Date? = null
) {
    val formattedRegistrationDate = Utils.getUserRegistrationFormattedDate(registrationDate)
}

val fakeRegisteredUsersList = (0..20).map { index ->
    User(
        userId = "123456",
        username = "Simple Chat",
        profilePicture = "",
        registrationDate = null
    )
}
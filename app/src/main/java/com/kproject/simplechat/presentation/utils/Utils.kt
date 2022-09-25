package com.kproject.simplechat.presentation.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    /**
     * Gets the current formatted date. If the date returned by Firebase is null, it returns
     * the current local date. There may be a difference between the date shown initially when
     * sending a message and the date returned by Firebase, as the timestamp is marked as
     * @ServerTimestamp and will be recorded by the date of the Firebase server.
     */
    fun getChatMessageFormattedDate(date: Date?): String {
        val currentDate = date ?: Calendar.getInstance().time
        val currentLocalDay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Calendar.getInstance().time)
        val currentTimestampDay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(currentDate)
        val dateFormat =
                if (currentLocalDay == currentTimestampDay) "HH:mm" else "HH:mm, dd MMM yyyy"
        return SimpleDateFormat(dateFormat, Locale.getDefault()).format(currentDate)
    }

    fun getUserRegistrationFormattedDate(date: Date?): String {
        val currentDate = date ?: Calendar.getInstance().time
        return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(currentDate)
    }
}
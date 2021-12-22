package com.kproject.simplechat.utils

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

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

    fun getCurrentUserId(): String = FirebaseAuth.getInstance().currentUser?.uid!!

    /**
     * Gets the current formatted date. If the date returned by Firebase is null, it returns
     * the current local date. There may be a difference between the date shown initially when
     * sending a message and the date returned by Firebase, as the timestamp is marked as
     * @ServerTimestamp and will be recorded by the date of the Firebase server.
     */
    fun getFormattedDate(date: Date?): String {
        val currentDate = date ?: Calendar.getInstance().time
        val currentLocalDay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Calendar.getInstance().time)
        val currentTimestampDay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(currentDate)
        val dateFormat =
                if (currentLocalDay == currentTimestampDay) "HH:mm" else "HH:mm (dd/MM/yyyy)"
        return SimpleDateFormat(dateFormat, Locale.getDefault()).format(currentDate)
    }
}
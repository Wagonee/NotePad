package com.example.notepad.presentation.utils

import java.text.DateFormat
import java.text.SimpleDateFormat

object DateFormatter {
    private val formatter = SimpleDateFormat.getDateInstance(DateFormat.SHORT)

    fun formatDateToString(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        return when {
            diff < 60 * 60000 -> {
                "Just now"
            }

            diff < 60 * 60000 * 24 -> {
                "Hours ago ${diff / 60 * 60000}"
            }

            else -> {
                formatter.format(timestamp)
            }
        }
    }
}
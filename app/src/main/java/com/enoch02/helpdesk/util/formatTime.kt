package com.enoch02.helpdesk.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTime(date: Date?): String {
    val formatter = SimpleDateFormat("EEE, dd MMM yyyy, hh:mm a", Locale.ENGLISH)

    return date?.let { formatter.format(it) } ?: ""
}

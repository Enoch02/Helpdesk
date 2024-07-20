package com.enoch02.helpdesk.util

import android.os.Build
import java.time.Instant
import java.util.Calendar
import java.util.Date

fun getCurrentDateTime(): Date? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Date.from(Instant.now()) else Calendar.getInstance().time
}
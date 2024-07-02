package com.enoch02.helpdesk.util

import android.content.Context
import android.content.Intent
import com.enoch02.helpdesk.MainActivity

fun restartActivity(context: Context) {
    val intent = Intent(context, MainActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    context.startActivity(intent)
}
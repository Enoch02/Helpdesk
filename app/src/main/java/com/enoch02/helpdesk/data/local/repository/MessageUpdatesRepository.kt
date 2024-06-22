package com.enoch02.helpdesk.data.local.repository

import android.content.Context

interface MessageUpdatesRepository {
    fun checkForUpdates(/*context: Context, */interval: Long = 300000L)

    fun cancelWork()
}
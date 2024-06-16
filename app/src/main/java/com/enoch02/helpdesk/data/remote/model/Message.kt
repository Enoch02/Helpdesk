package com.enoch02.helpdesk.data.remote.model

import java.util.Date

data class Message(
    val messageText: String? = null,
    val sentAt: Date? = null,
    val sentBy: String? = null
)

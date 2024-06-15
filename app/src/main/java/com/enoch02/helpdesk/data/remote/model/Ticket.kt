package com.enoch02.helpdesk.data.remote.model

import java.util.Date

data class Ticket(
    val uid: String? = null,
    val staffID: String? = null,
    val subject: String? = null,
    val description: String? = null,
    val status: String? = null,
    val priority: String? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val closedAt: Date? = null,
    //TODO
    //val attachments: List<String> = emptyList()
    //val history: List<String> = emptyList()
)

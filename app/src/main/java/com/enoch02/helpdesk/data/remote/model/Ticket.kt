package com.enoch02.helpdesk.data.remote.model

import java.util.Date
import java.util.UUID

data class Ticket(
    val uid: String? = null,
    val ticketID: String? = UUID.randomUUID().toString(),
    val staffID: String? = null,
    val subject: String? = null,
    val category: String? = null,
    val description: String? = null,
    val status: String? = null,
    val priority: String? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val closedAt: Date? = null,
    val chatID: String? = null
    //TODO
    //val history: List<String> = emptyList()
)

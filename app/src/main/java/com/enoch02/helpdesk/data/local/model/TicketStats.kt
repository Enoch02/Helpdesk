package com.enoch02.helpdesk.data.local.model

data class TicketStats(
    val total: Int = 0,
    val open: Int = 0,
    val closed: Int = 0,
    val unassigned: Int = 0,
)

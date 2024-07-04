package com.enoch02.helpdesk.util

import com.enoch02.helpdesk.data.local.model.toPriority
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.model.Tickets

/**
 * [ticketsObj] - tickets to sort
 * [order] - sorting order in [SORTING_CRITERIA]
 * Sort the [Ticket] in [Tickets] and returned a sorted [MutableList]
 * */
fun sortTickets(tickets: List<Ticket>, order: String): MutableList<Ticket> {
    return when (order) {
        SORTING_CRITERIA[0] -> {
            tickets.sortedBy { it.subject }.toMutableList()
        }

        SORTING_CRITERIA[1] -> {
            tickets.sortedByDescending { it.subject }.toMutableList()
        }

        SORTING_CRITERIA[2] -> {
            tickets.sortedBy { it.createdAt }.toMutableList()
        }

        SORTING_CRITERIA[3] -> {
            tickets.sortedByDescending { it.createdAt }.toMutableList()
        }

        SORTING_CRITERIA[4] -> {
            tickets.sortedByDescending { it.priority?.toPriority() }.toMutableList()
        }

        SORTING_CRITERIA[5] -> {
            tickets.sortedBy { it.priority?.toPriority() }.toMutableList()
        }

        else -> {
            tickets.toMutableList()
        }
    }
}
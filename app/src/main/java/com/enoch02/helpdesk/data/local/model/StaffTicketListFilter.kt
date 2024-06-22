package com.enoch02.helpdesk.data.local.model

sealed class Filter(val value: String) {
    data object All: Filter("All")
    data object Open: Filter("Open")
    data object Closed: Filter("Closed")
    data object Unassigned: Filter("Unassigned")
    data object AssignedToMe: Filter("ToMe")
}
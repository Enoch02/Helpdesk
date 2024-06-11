package com.enoch02.helpdesk.ui.screen.student.ticket_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TicketListViewModel : ViewModel() {
    var query by mutableStateOf("")
    var searchActive by mutableStateOf(false)

    fun updateQuery(newQuery: String) {
        query = newQuery
    }

    fun updateSearchStatus(newStatus: Boolean) {
        searchActive = newStatus
    }
}
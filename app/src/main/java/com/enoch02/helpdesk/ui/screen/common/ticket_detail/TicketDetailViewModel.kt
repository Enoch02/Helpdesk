package com.enoch02.helpdesk.ui.screen.common.ticket_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TicketDetailViewModel : ViewModel() {
    var subject by mutableStateOf("")
    var category by mutableStateOf("")
    var priority by mutableStateOf("")
    var status by mutableStateOf("")
    var creationDate by mutableStateOf("")
    var description by mutableStateOf("")


}
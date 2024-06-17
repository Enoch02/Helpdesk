package com.enoch02.helpdesk.ui.screen.staff.ticket_list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.model.Tickets
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaffTicketListViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {
    var contentState by mutableStateOf(ContentState.LOADING)
    var errorMessage by mutableStateOf("")
    var query by mutableStateOf("")
    var searchActive by mutableStateOf(false)

    var tickets by mutableStateOf(Tickets())
    var searchResult = mutableStateListOf<Ticket>()

    fun clearQuery() {
        query = ""
    }

    fun updateQuery(newQuery: String) {
        query = newQuery
    }

    fun updateSearchStatus(newStatus: Boolean) {
        searchActive = newStatus
    }

    fun getTickets(filter: String) {
        viewModelScope.launch {
            firestoreRepository.getTickets()
                .onSuccess {
                    when (filter) {
                        "all" -> {
                            tickets = tickets.copy(tickets = it.toMutableList())
                        }
                        "Unassigned" -> {
                            tickets = tickets.copy(
                                tickets = it.filter { ticket -> ticket.staffID.isNullOrBlank() }
                                    .toMutableList()
                            )
                        }
                        else -> {
                            tickets = tickets.copy(
                                tickets = it.filter { ticket -> ticket.status == filter }
                                    .toMutableList()
                            )
                        }
                    }

                    contentState = ContentState.COMPLETED
                }
                .onFailure {
                    contentState = ContentState.FAILURE
                    errorMessage = it.message.toString()
                }
        }
    }

    fun startSearch() {
        if (searchResult.isNotEmpty()) {
            searchResult.clear()
        }
        val temp = tickets.tickets?.filter {
            it.subject?.lowercase()?.contains(query.lowercase()) ?: false
        }

        if (temp != null) {
            searchResult.addAll(temp)
        }
    }
}
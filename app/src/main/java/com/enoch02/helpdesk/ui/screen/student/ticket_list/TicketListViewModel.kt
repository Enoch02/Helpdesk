package com.enoch02.helpdesk.ui.screen.student.ticket_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.model.Tickets
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketListViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    var query by mutableStateOf("")
    var searchActive by mutableStateOf(false)
    var tickets by mutableStateOf(Tickets())
    var searchResult = mutableStateListOf<Ticket>()

    init {
        getTickets()
    }

    fun clearQuery() {
        query = ""
    }

    fun updateQuery(newQuery: String) {
        query = newQuery
    }

    fun updateSearchStatus(newStatus: Boolean) {
        searchActive = newStatus
    }

    fun getTickets() {
        viewModelScope.launch {
            firestoreRepository.getTickets(authRepository.getUID())
                .onSuccess {
                    tickets = it
                }
                .onFailure {
                    /*TODO*/
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
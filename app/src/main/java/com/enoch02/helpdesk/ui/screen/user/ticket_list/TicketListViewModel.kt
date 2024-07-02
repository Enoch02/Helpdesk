package com.enoch02.helpdesk.ui.screen.user.ticket_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.local.model.toPriority
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.model.Tickets
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import com.enoch02.helpdesk.util.SORTING_CRITERIA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketListViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    var contentState by mutableStateOf(ContentState.LOADING)
    var errorMessage by mutableStateOf("")
    var query by mutableStateOf("")
    var searchActive by mutableStateOf(false)
    var tickets by mutableStateOf(Tickets())
    var searchResult = mutableStateListOf<Ticket>()

    var isRefreshing by mutableStateOf(false)

    var currentSorting by mutableStateOf(SORTING_CRITERIA[2])

    fun onRefresh(filter: String) {
        isRefreshing = true
        getTickets(filter)
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

    fun updateCurrentSorting(newSorting: String) {
        currentSorting = newSorting
    }

    private fun getTickets(filter: String) {
        viewModelScope.launch {
            firestoreRepository.getTickets(authRepository.getUID())
                .onSuccess {
                    tickets = if (filter == "all") {
                        it
                    } else {
                        it.copy(
                            tickets = it.tickets?.filter { ticket ->
                                ticket.status == filter
                            }?.toMutableList()
                        )
                    }

                    contentState = ContentState.COMPLETED
                    isRefreshing = false
                }
                .onFailure {
                    contentState = ContentState.FAILURE
                    isRefreshing = false
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

    fun sortTickets(order: String) {
        val temp = tickets.tickets

        when (order) {
            SORTING_CRITERIA[0] -> {
                temp?.sortBy { it.subject }
            }

            SORTING_CRITERIA[1] -> {
                temp?.sortByDescending { it.subject }
            }

            SORTING_CRITERIA[2] -> {
                temp?.sortBy { it.createdAt }
            }

            SORTING_CRITERIA[3] -> {
                temp?.sortByDescending { it.createdAt }
            }

            SORTING_CRITERIA[4] -> {
                temp?.sortByDescending { it.priority?.toPriority() }
            }

            SORTING_CRITERIA[5] -> {
                temp?.sortBy { it.priority?.toPriority() }
            }
        }

        tickets = tickets.copy(tickets = temp)
    }

    fun getAndSortTickets(filter: String, sorting: String) {
        getTickets(filter)
        sortTickets(sorting)
    }
}
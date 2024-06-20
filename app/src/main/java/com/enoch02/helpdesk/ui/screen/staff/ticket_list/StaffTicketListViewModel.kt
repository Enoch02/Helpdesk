package com.enoch02.helpdesk.ui.screen.staff.ticket_list

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.model.Tickets
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.cloud_storage.CloudStorageRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StaffTicketListViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository,
    private val cloudStorageRepository: CloudStorageRepository
) : ViewModel() {
    var contentState by mutableStateOf(ContentState.LOADING)
    var errorMessage by mutableStateOf("")
    var query by mutableStateOf("")
    var searchActive by mutableStateOf(false)

    var tickets by mutableStateOf(Tickets())
    var searchResult = mutableStateListOf<Ticket>()

    var isRefreshing by mutableStateOf(false)

    var reassignDialogContentState by mutableStateOf(ContentState.LOADING)
    var staff by mutableStateOf(emptyList<UserData>())
    // It should be safe to make the index zero here, users can not access the buttons when the lazycolumn is empty
    var selectedStaffIndex by mutableIntStateOf(0)
    var selectedTicketIndex by mutableIntStateOf(0)
    var profilePictures by mutableStateOf(emptyList<Uri?>())

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

    fun updateSelectedStaff(index: Int) {
        selectedStaffIndex = index
    }

    fun getTickets(filter: String) {
        viewModelScope.launch(Dispatchers.IO) {
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
                    isRefreshing = false
                }
                .onFailure {
                    contentState = ContentState.FAILURE
                    errorMessage = it.message.toString()
                    isRefreshing = false
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

    fun getUID() = authRepository.getUID()

    fun assignTicketToSelf(context: Context, sid: String = getUID(), ticket: Ticket) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.updateTicket(
                uid = ticket.uid.toString(),
                tid = ticket.ticketID.toString(),
                newTicket = ticket.copy(
                    staffID = sid
                )
            ).onSuccess {
                withContext(Dispatchers.Main) {
                    isRefreshing = true
                    Toast.makeText(context, "Ticket Assigned", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getUsers() {
        reassignDialogContentState = ContentState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getUsers()
                .onSuccess { userData ->
                    staff = userData.filter { it.role == "Staff" && it.userID != getUID()}
                    getProfilePictures()
                    reassignDialogContentState = ContentState.COMPLETED
                }
        }
    }

    fun assignTicket(context: Context, sid: String, ticket: Ticket) {
        assignTicketToSelf(context, sid, ticket)
    }

    private fun getProfilePictures() {
        val temp = mutableListOf<Uri?>()

        viewModelScope.launch {
            staff.forEach { data ->
                data.userID?.let { id ->
                    cloudStorageRepository.getProfilePicture(id)
                        .onSuccess {
                            temp.add(it)
                        }
                        .onFailure {
                            temp.add(null)
                        }
                }
            }

            profilePictures = temp
        }
    }
}
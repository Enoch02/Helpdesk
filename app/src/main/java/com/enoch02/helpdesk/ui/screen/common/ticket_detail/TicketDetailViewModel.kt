package com.enoch02.helpdesk.ui.screen.common.ticket_detail

import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.model.Chat
import com.enoch02.helpdesk.data.remote.model.Members
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TicketDetailViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) :
    ViewModel() {
    private var theTicket by mutableStateOf(Ticket())

    var contentState by mutableStateOf(ContentState.LOADING)
    var errorMessage by mutableStateOf("")
    var subject by mutableStateOf("")
    var category by mutableStateOf("")
    var priority by mutableStateOf("")
    var status by mutableStateOf("")
    var creationDate by mutableStateOf("")
    var description by mutableStateOf("")
    var assignedTo by mutableStateOf("")
    var chatID by mutableStateOf("")

    var navigateToChatScreen by mutableStateOf(false)


    fun getTicket(uid: String, tid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getTicket(uid, tid)
                .onSuccess {
                    theTicket = it

                    subject = it.subject.toString()
                    category = it.category.toString()
                    priority = it.priority.toString()
                    status = it.status.toString()
                    creationDate = it.createdAt.toString()
                    description = it.description.toString()
                    assignedTo = it.staffID.toString()
                    chatID = it.chatID ?: ""

                    contentState = ContentState.COMPLETED
                }
                .onFailure {
                    contentState = ContentState.FAILURE
                    errorMessage = it.message.toString()
                }
        }
    }

    fun closeTicket(uid: String, tid: String, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            contentState = ContentState.LOADING
            firestoreRepository.closeTicket(uid, tid)
                .onSuccess {
                    onSuccess()
                    contentState = ContentState.COMPLETED
                }
        }
    }

    fun reopenTicket(uid: String, tid: String, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            contentState = ContentState.LOADING
            firestoreRepository.openTicket(uid, tid)
                .onSuccess {
                    onSuccess()
                    contentState = ContentState.COMPLETED
                }
        }
    }

    /*TODO: what if the chat already exists?*/
    fun startNewChat(uid: String, tid: String, staffID: String) {
        viewModelScope.launch {
            firestoreRepository.startNewChat(
                tid = tid,
                chat = Chat(
                    createdAt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Date.from(
                        Instant.now()
                    ) else Calendar.getInstance().time,
                    startedBy = uid,
                    members = Members(userID = uid, staffID = staffID)
                )
            ).onSuccess {
                // update the ticket with the chatID
                firestoreRepository.updateTicket(
                    uid = uid,
                    tid = tid,
                    newTicket = theTicket.copy(chatID = it)
                )
                chatID = it
                navigateToChatScreen = true
            }
        }
    }
}
package com.enoch02.helpdesk.ui.screen.common.ticket_detail

import android.net.Uri
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.model.Chat
import com.enoch02.helpdesk.data.remote.model.Members
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.cloud_storage.CloudStorageRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import com.enoch02.helpdesk.util.DEFAULT_DISPLAY_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TicketDetailViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val authRepository: FirebaseAuthRepository,
    private val cloudStorageRepository: CloudStorageRepository,
) :
    ViewModel() {
    private var theTicket by mutableStateOf(Ticket())
    var userData by mutableStateOf(UserData())

    var contentState by mutableStateOf(ContentState.LOADING)
    var errorMessage by mutableStateOf("")
    var subject by mutableStateOf("")
    var category by mutableStateOf("")
    var priority by mutableStateOf("")
    var status by mutableStateOf("")
    var creationDate by mutableStateOf("")
    var description by mutableStateOf("")
    var staffID by mutableStateOf("")
    var assignedTo by mutableStateOf("")
    var createdBy by mutableStateOf("")
    var chatID by mutableStateOf("")
    var ticketID by mutableStateOf("")
    var attachments by mutableStateOf(emptyList<Uri>())
    private var feedbackGiven by mutableStateOf(false)

    var navigateToChatScreen by mutableStateOf(false)
    var showFeedbackDialog by mutableStateOf(false)
    var ticketOwnerId by mutableStateOf("")


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
                    staffID = it.staffID ?: ""

                    firestoreRepository.getUserName(it.staffID.toString())
                        .onSuccess { name ->
                            assignedTo = name
                        }
                        .onFailure { _ ->
                            assignedTo = it.staffID.toString()
                        }

                    firestoreRepository.getUserName(it.uid.toString())
                        .onSuccess { name ->
                            createdBy = name
                        }
                        .onFailure { _ ->
                            createdBy = it.staffID.toString()
                        }

                    cloudStorageRepository.getTicketAttachments(it.ticketID.toString())
                        .onSuccess { ticketAttachments ->
                            attachments = ticketAttachments
                        }

                    chatID = it.chatID ?: ""
                    ticketID = it.ticketID ?: ""
                    feedbackGiven = it.feedBackGiven ?: false
                    ticketOwnerId = it.uid.toString()

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

                    if (!feedbackGiven && userData.role == DEFAULT_DISPLAY_NAME) {
                        showFeedbackDialog = true
                    }
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

    fun startNewChat(uid: String, tid: String, staffID: String) {
        viewModelScope.launch {
            firestoreRepository.startNewChat(
                chat = Chat(
                    ticketID = tid,
                    createdAt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Date.from(
                        Instant.now()
                    ) else Calendar.getInstance().time,
                    startedBy = uid,
                    members = Members(userID = uid, staffID = staffID),
                    messages = emptyList()
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

    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getUserData(authRepository.getUID())
                .onSuccess {
                    if (it != null) {
                        userData = it
                    }
                }
        }
    }
}
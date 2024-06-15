package com.enoch02.helpdesk.ui.screen.common.ticket_detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketDetailViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) :
    ViewModel() {
    var contentState by mutableStateOf(ContentState.LOADING)
    var errorMessage by mutableStateOf("")
    var subject by mutableStateOf("")
    var category by mutableStateOf("")
    var priority by mutableStateOf("")
    var status by mutableStateOf("")
    var creationDate by mutableStateOf("")
    var description by mutableStateOf("")


    fun getTicket(uid: String, tid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getTicket(uid, tid)
                .onSuccess {
                    subject = it.subject.toString()
                    category = it.category.toString()
                    priority = it.priority.toString()
                    status = it.status.toString()
                    creationDate = it.createdAt.toString()
                    description = it.description.toString()

                    contentState = ContentState.COMPLETED
                }
                .onFailure {
                    contentState = ContentState.FAILURE
                    errorMessage = it.message.toString()
                }
        }
    }

    fun closeTicket(uid: String, tid: String, onSuccess:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            contentState = ContentState.LOADING
            firestoreRepository.closeTicket(uid, tid)
                .onSuccess {
                    onSuccess()
                    contentState = ContentState.COMPLETED
                }
        }
    }

    fun reopenTicket(uid: String, tid: String, onSuccess:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            contentState = ContentState.LOADING
            firestoreRepository.openTicket(uid, tid)
                .onSuccess {
                    onSuccess()
                    contentState = ContentState.COMPLETED
                }
        }
    }
}
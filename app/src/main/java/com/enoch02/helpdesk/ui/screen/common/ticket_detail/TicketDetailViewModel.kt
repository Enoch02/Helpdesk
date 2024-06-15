package com.enoch02.helpdesk.ui.screen.common.ticket_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketDetailViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) :
    ViewModel() {
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
                }
        }
    }

    fun closeTicket(uid: String, tid: String, onSuccess:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.closeTicket(uid, tid)
                .onSuccess {
                    onSuccess()
                }
        }
    }

    fun reopenTicket(uid: String, tid: String, onSuccess:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.openTicket(uid, tid)
                .onSuccess {
                    onSuccess()
                }
        }
    }
}
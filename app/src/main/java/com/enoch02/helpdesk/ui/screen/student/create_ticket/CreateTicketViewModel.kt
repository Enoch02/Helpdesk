package com.enoch02.helpdesk.ui.screen.student.create_ticket

import android.net.Uri
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.Category
import com.enoch02.helpdesk.data.local.model.Priority
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CreateTicketViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository
) :
    ViewModel() {
    var subject by mutableStateOf("")
    var category by mutableStateOf(Category.builtInCategories[0])
    var priority by mutableStateOf(Priority.LOW)
    var description by mutableStateOf("")
    var selectedAttachments = mutableStateListOf<Uri>()

    fun updateSubject(newSubject: String) {
        subject = newSubject
    }

    fun updateCategory(newCategory: String) {
        category = newCategory
    }

    fun updatePriority(newPriority: Priority) {
        priority = newPriority
    }

    fun updateDescription(newDesc: String) {
        description = newDesc
    }

    fun removeAttachment(index: Int) {
        selectedAttachments.removeAt(index)
    }

    fun submitTicket(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val newTicket = Ticket(
                uid = authRepository.getUID(),
                subject = subject,
                description = description,
                status = "Open",
                priority = priority.stringify(),
                createdAt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Date.from(Instant.now()) else Calendar.getInstance().time
            )

            firestoreRepository.createTicket(newTicket)
                .onSuccess {
                    onSuccess()
                }
                .onFailure {
                    onFailure(it.message.toString())
                }
        }
    }
}
package com.enoch02.helpdesk.ui.screen.student.create_ticket

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.enoch02.helpdesk.data.model.Category
import com.enoch02.helpdesk.data.model.Priority

class CreateTicketViewModel : ViewModel() {
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

    fun submitTicket() {

    }
}
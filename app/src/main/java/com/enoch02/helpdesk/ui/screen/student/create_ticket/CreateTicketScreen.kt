package com.enoch02.helpdesk.ui.screen.student.create_ticket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.data.model.Category
import com.enoch02.helpdesk.data.model.Priority
import com.enoch02.helpdesk.data.model.toPriority
import com.enoch02.helpdesk.ui.screen.student.create_ticket.component.AttachmentSelector
import com.enoch02.helpdesk.ui.screen.student.create_ticket.component.FormDropdown
import com.enoch02.helpdesk.ui.screen.student.create_ticket.component.FormTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketScreen(
    navController: NavController,
    viewModel: CreateTicketViewModel = viewModel()
) {
    val subject = viewModel.subject
    val category = viewModel.category
    val priority = viewModel.priority
    val description = viewModel.description
    val selectedAttachments = viewModel.selectedAttachments

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create New Ticket") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                content = {
                    FormTextField(
                        label = "Subject",
                        value = subject,
                        onValueChange = {
                            viewModel.updateSubject(it)
                        },
                        maxLines = 1
                    )


                    FormDropdown(
                        selection = viewModel.category,
                        options = Category.builtInCategories,
                        allowCustomOptions = true,
                        onSelectionChange = {
                            viewModel.updateCategory(it)
                        }
                    )


                    FormDropdown(
                        selection = viewModel.priority.stringify(),
                        options = Priority.entries.map { it.stringify() },
                        allowCustomOptions = false,
                        onSelectionChange = {
                            viewModel.updatePriority(it.toPriority())
                        }
                    )


                    FormTextField(
                        label = "Description",
                        value = description,
                        onValueChange = {
                            viewModel.updateDescription(it)
                        },
                        maxLines = 5
                    )

                    AttachmentSelector(
                        modifier = Modifier,
                        selectedImages = selectedAttachments,
                        onRemoveAttachmentAt = {
                            viewModel.removeAttachment(it)
                        }
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    //TODO: this is being pushed out of view when attachments are added...
                    Button(
                        onClick = { viewModel.submitTicket() },
                        content = {
                            Text(text = "Submit Ticket")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(8.dp)
            )
        }
    )
}
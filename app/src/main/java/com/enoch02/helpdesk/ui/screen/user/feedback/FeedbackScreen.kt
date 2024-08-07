package com.enoch02.helpdesk.ui.screen.user.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.ui.screen.common.component.LoadingView
import com.enoch02.helpdesk.ui.screen.user.feedback.component.RatingPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    ticketOwnerId: String,
    ticketId: String,
    navController: NavController,
    viewModel: FeedbackViewModel = hiltViewModel(),
) {
    val rating = viewModel.rating
    val additionalFeedback = viewModel.additionalFeedback

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Feedback") },
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
            when (viewModel.contentState) {
                ContentState.LOADING -> {
                    LoadingView(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }

                ContentState.COMPLETED -> {
                    Column(
                        content = {
                            Text(
                                text = "How would you rate your experience?",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            RatingPicker(
                                rating = rating,
                                onRatingChanged = { viewModel.updateRating(it) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Additional Feedback",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.labelLarge.fontSize
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = additionalFeedback,
                                onValueChange = { viewModel.updateAdditionalFeedback(it) },
                                label = { Text(text = "Message") },
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences,
                                    autoCorrect = true
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { viewModel.sendFeedback(ticketOwnerId, ticketId) },
                                content = {
                                    Text(text = "Submit Feedback")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            )
                        },
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(8.dp)
                    )

                    if (viewModel.feedbackSent) {
                        navController.popBackStack()
                    }
                }

                ContentState.FAILURE -> {
                    //TODO
                }
            }
        }
    )
}
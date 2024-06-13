package com.enoch02.helpdesk.ui.screen.student.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.ui.screen.student.feedback.component.RatingPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(navController: NavController, viewModel: FeedbackViewModel = viewModel()) {
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
                        onClick = { /*TODO*/ },
                        content = {
                            Text(text = "Submit Feedback")
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                },
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(8.dp)
            )
        }
    )
}
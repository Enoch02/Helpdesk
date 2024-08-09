package com.enoch02.helpdesk.ui.screen.staff.feedback_viewer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.ui.screen.staff.feedback_viewer.component.FeedbackBarChart
import com.enoch02.helpdesk.ui.screen.staff.feedback_viewer.component.RatingBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackViewerScreen(
    navController: NavController,
    viewerViewModel: FeedbackViewerViewModel = hiltViewModel(),
) {
    val feedbacks = viewerViewModel.feedbacks

    SideEffect {
        viewerViewModel.getFeedbacks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Feedback & Ratings")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            if (feedbacks.isEmpty()) {
                Text(
                    text = "Nothing here yet",
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    content = {
                        item {
                            FeedbackBarChart(
                                feedbackData = viewerViewModel.createFeedbackData(
                                    feedbacks
                                )
                            )
                        }

                        item {
                            Text(
                                text = "Anonymous Feedbacks",
                                textAlign = TextAlign.Start,
                                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                fontWeight = MaterialTheme.typography.headlineLarge.fontWeight,
                                lineHeight = MaterialTheme.typography.headlineLarge.lineHeight,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                        }

                        items(
                            count = feedbacks.size,
                            itemContent = { index ->
                                Card(
                                    content = {
                                        Text(
                                            text = feedbacks[index].additionalFeedback.toString(),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp)
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        feedbacks[index].rating?.let {
                                            RatingBar(
                                                rating = it,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    },
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        )
                    }
                )
            }
        }
    )
}
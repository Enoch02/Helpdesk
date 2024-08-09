package com.enoch02.helpdesk.ui.screen.staff.feedback_viewer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.remote.model.Feedback
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import com.enoch02.helpdesk.ui.screen.staff.feedback_viewer.component.FeedbackData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewerViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) :
    ViewModel() {
    var feedbacks by mutableStateOf(emptyList<Feedback>())

    fun getFeedbacks() {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getFeedbacks().onSuccess {
                feedbacks = it
            }
        }
    }

    fun createFeedbackData(feedbacks: List<Feedback>): List<FeedbackData> {
        val feedbackData = mutableListOf<FeedbackData>()

        for (i in 1..5) {
            feedbackData.add(FeedbackData(i, feedbacks.count { it.rating == i }))
        }

        return feedbackData.toList()
    }
}
package com.enoch02.helpdesk.ui.screen.user.feedback

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.model.Feedback
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {
    var rating by mutableIntStateOf(0)
    var additionalFeedback by mutableStateOf("")
    var feedbackSent by mutableStateOf(false)

    var contentState by mutableStateOf(ContentState.COMPLETED)

    fun updateRating(newRating: Int) {
        rating = newRating
    }

    fun updateAdditionalFeedback(newFeedback: String) {
        additionalFeedback = newFeedback
    }

    fun sendFeedback(uid: String, tid: String) {
        contentState = ContentState.LOADING
        val feedback = Feedback(rating = rating, additionalFeedback = additionalFeedback)

        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.sendFeedback(feedback)
                .onSuccess {
                    updateTicketFeedbackStatus(uid, tid)
                }
                .onFailure {
                    //TODO
                    contentState = ContentState.FAILURE
                }
        }
    }

    private fun updateTicketFeedbackStatus(uid: String, tid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getTicket(uid, tid)
                .onSuccess {
                    val newTicket = it.copy(feedBackGiven = true)

                    firestoreRepository.updateTicket(uid, tid, newTicket)
                        .onSuccess {
                            feedbackSent = true
                            contentState = ContentState.COMPLETED
                        }
                }
        }
    }
}
package com.enoch02.helpdesk.ui.screen.user.feedback

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FeedbackViewModel : ViewModel() {
    var rating by mutableIntStateOf(0)
    var additionalFeedback by mutableStateOf("")

    fun updateRating(newRating: Int) {
        rating = newRating
    }

    fun updateAdditionalFeedback(newFeedback: String) {
        additionalFeedback = newFeedback
    }
}
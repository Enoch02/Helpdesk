package com.enoch02.helpdesk

import com.enoch02.helpdesk.data.remote.model.Feedback
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FeedbackSubmissionTest {
    private val mockFirestoreRepo: FirestoreRepository = mockk<FirestoreRepository>()
    private val feedback = Feedback(rating = 5, additionalFeedback = null)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { runBlocking { mockFirestoreRepo.sendFeedback(feedback) } } returns Result.success(
            Unit
        )
    }

    @Test
    fun sendFeedback() {
        runBlocking {
            mockFirestoreRepo.sendFeedback(feedback)
                .onSuccess {
                    assert(true)
                }
                .onFailure {
                    assert(false)
                }
        }
    }
}
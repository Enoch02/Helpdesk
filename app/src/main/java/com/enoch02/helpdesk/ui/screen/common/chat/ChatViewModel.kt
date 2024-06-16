package com.enoch02.helpdesk.ui.screen.common.chat

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.cloud_storage.CloudStorageRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import com.enoch02.helpdesk.ui.screen.common.chat.component.BubbleOwner
import com.enoch02.helpdesk.ui.screen.common.chat.component.BubbleType
import com.enoch02.helpdesk.ui.screen.common.chat.component.DemoMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val cloudStorageRepository: CloudStorageRepository,
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {
    var message by mutableStateOf("")
    var temp = mutableStateListOf<DemoMessage>()
    val selectedAttachments = mutableStateListOf<Uri>()

    init {
        for (i in 0..30) {
            temp.add(
                DemoMessage(
                    text = "Message $i",
                    owner = if (i % 2 == 0) BubbleOwner.REMOTE else BubbleOwner.LOCAL,
                    type = BubbleType.TEXT,
                    url = ""
                )
            )
        }
    }

    fun updateMessage(newMessage: String) {
        message = newMessage
    }

    //TODO: more demos
    fun sendMessage(): Result<Unit> {
        if (message.isEmpty() && selectedAttachments.isEmpty()) {
            return Result.failure(Exception("Enter some text"))
        }

        if (selectedAttachments.isEmpty()) {
            temp.add(
                DemoMessage(
                    text = message,
                    owner = BubbleOwner.LOCAL,
                    type = BubbleType.TEXT,
                    url = ""
                )
            )
        } else {
            selectedAttachments.forEach {
                temp.add(
                    DemoMessage(
                        text = "",
                        owner = BubbleOwner.LOCAL,
                        type = BubbleType.IMAGE,
                        url = it.toString()
                    )
                )
            }
        }
        message = ""
        selectedAttachments.clear()

        return Result.success(Unit)
    }

    fun removeAttachment(index: Int) {
        selectedAttachments.removeAt(index)
    }
}
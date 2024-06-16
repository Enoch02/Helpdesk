package com.enoch02.helpdesk.ui.screen.common.chat

import android.net.Uri
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.remote.model.Chat
import com.enoch02.helpdesk.data.remote.model.Message
import com.enoch02.helpdesk.data.remote.model.MessageType
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.cloud_storage.CloudStorageRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val cloudStorageRepository: CloudStorageRepository,
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {
    var message by mutableStateOf("")
    val selectedAttachments = mutableStateListOf<Uri>()

    var chat by mutableStateOf<Chat?>(null)

    fun updateMessage(newMessage: String) {
        message = newMessage
    }

    fun getUID() = authRepository.getUID()

    /*fun sendMessageDemo(): Result<Unit> {
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
    }*/

    fun getChat(cid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getChat(cid = cid)
                .onSuccess {
                    chat = it
                }
        }
    }

    fun sendMessage(cid: String): Result<Unit> {
        if (message.isEmpty() && selectedAttachments.isEmpty()) {
            return Result.failure(Exception("Enter some text"))
        }
        viewModelScope.launch {
            firestoreRepository.sendMessage(
                cid = cid,
                newMessage = Message(
                    messageText = message,
                    sentAt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Date.from(
                        Instant.now()
                    ) else Calendar.getInstance().time,
                    sentBy = authRepository.getUID(),
                    type = MessageType.TEXT
                )
            )
                .onSuccess {
                    getChat(cid)
                    message = ""
                    selectedAttachments.clear()
                }
        }

        return Result.success(Unit)
    }

    fun removeAttachment(index: Int) {
        selectedAttachments.removeAt(index)
    }
}
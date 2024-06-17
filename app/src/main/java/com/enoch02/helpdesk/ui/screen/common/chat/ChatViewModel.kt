package com.enoch02.helpdesk.ui.screen.common.chat

import android.net.Uri
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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
) : ViewModel(), DefaultLifecycleObserver {
    var message by mutableStateOf("")
    val selectedAttachments = mutableStateListOf<Uri>()

    var chat by mutableStateOf<Chat?>(null)

    var chatUpdateJob: Job? = null

    fun updateMessage(newMessage: String) {
        message = newMessage
    }

    fun getUID() = authRepository.getUID()

    fun getChat(cid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getChat(cid = cid)
                .onSuccess {
                    chat = it
                }
        }
    }

    fun updateChat(cid: String) {
        chatUpdateJob = viewModelScope.launch {
            while (isActive) {
                getChat(cid = cid)

                delay(1000)
            }
        }
    }

    private fun stopChatUpdate() {
        chatUpdateJob?.cancel()
    }

    private fun resumeChatUpdate() {
        chatUpdateJob?.start()
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

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        stopChatUpdate()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        resumeChatUpdate()
    }
}
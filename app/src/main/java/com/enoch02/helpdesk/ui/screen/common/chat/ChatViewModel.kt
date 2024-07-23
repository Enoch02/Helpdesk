package com.enoch02.helpdesk.ui.screen.common.chat

import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
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
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.UUID
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
    var recipientName by mutableStateOf("")
    var recipientProfilePic: Uri? by mutableStateOf(null)
    private var chatPictures by mutableStateOf(emptyList<Uri?>())

    private var chatUpdateJob: Job? = null
    private var readStatusUpdateJob: Job? = null

    fun updateMessage(newMessage: String) {
        message = newMessage
    }

    fun getUID() = authRepository.getUID()

    fun getChat(cid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getChat(cid = cid)
                .onSuccess {
                    chat = it
                    getChatPictures(cid)
                }
        }
    }

    private fun getChatPictures(cid: String) {
        val temp = mutableListOf<Uri?>()

        viewModelScope.launch(Dispatchers.IO) {
            chat?.messages?.forEach { message ->
                if (message.imageId != null) {
                    cloudStorageRepository.getChatImage(
                        chatID = cid,
                        imageId = message.imageId
                    )
                        .onSuccess {
                            temp.add(it)
                        }
                        .onFailure {
                            temp.add(null)
                        }
                } else {
                    temp.add(null)
                }
            }

            chatPictures = temp
        }
    }

    fun getPictureAt(index: Int): Uri? {
        return try {
            chatPictures[index]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    fun updateChat(cid: String) {
        chatUpdateJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                getChat(cid = cid)

                delay(1000)
            }
        }

        updateReadStatus(cid)
    }

    private fun updateReadStatus(cid: String) {
        readStatusUpdateJob = viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.changeMessageReadStatus(
                uid = authRepository.getUID(),
                cid = cid
            )
        }
    }

    private fun stopUpdate() {
        chatUpdateJob?.cancel()
        readStatusUpdateJob?.cancel()
    }

    private fun resumeUpdate() {
        chatUpdateJob?.start()
        readStatusUpdateJob?.start()
    }

    fun sendMessage(context: Context, cid: String): Result<Unit> {
        if (message.isEmpty() && selectedAttachments.isEmpty()) {
            return Result.failure(Exception("Enter some text"))
        }

        viewModelScope.launch {
            //TODO: add progress indicators?
            if (selectedAttachments.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Uploading Images", Toast.LENGTH_SHORT).show()
                }

                selectedAttachments.forEachIndexed { index, uri ->
                    val id = UUID.randomUUID().toString()
                    val sentAt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Date.from(
                        Instant.now()
                    ) else Calendar.getInstance().time
                    val temp =
                        // only the 1st attachment gets the text if attachments > 1
                        Message(
                            messageText = if (index == 0 && message.isNotBlank()) message else "",
                            imageId = id,
                            sentAt = sentAt,
                            sentBy = authRepository.getUID(),
                            type = if (index == 0) MessageType.IMAGE_AND_TEXT else MessageType.IMAGE
                        )


                    cloudStorageRepository.uploadChatImage(
                        uri = uri,
                        chatId = cid,
                        imageId = temp.imageId!!
                    )
                        .onSuccess {
                            firestoreRepository.sendMessage(cid, temp)
                                .onSuccess {
                                    getChat(cid)
                                    message = ""
                                }

                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Upload Complete!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                }

                selectedAttachments.clear()
            } else {
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
                    }
            }
        }

        return Result.success(Unit)
    }

    fun removeAttachment(index: Int) {
        selectedAttachments.removeAt(index)
    }

    fun getRecipientName() {
        if (recipientName.isEmpty()) { // should only get it once for the chat
            viewModelScope.launch(Dispatchers.IO) {
                if (chat?.members?.userID == getUID()) {
                    chat?.members?.staffID?.let {
                        firestoreRepository.getUserName(it)
                            .onSuccess { name ->
                                recipientName = name
                            }

                        cloudStorageRepository.getProfilePicture(it)
                            .onSuccess { pic ->
                                recipientProfilePic = pic
                            }
                    }
                } else {
                    chat?.members?.userID?.let {
                        firestoreRepository.getUserName(it)
                            .onSuccess { name ->
                                recipientName = name
                            }

                        cloudStorageRepository.getProfilePicture(it)
                            .onSuccess { pic ->
                                recipientProfilePic = pic
                            }
                    }
                }
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        stopUpdate()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        resumeUpdate()
    }
}
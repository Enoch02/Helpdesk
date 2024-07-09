package com.enoch02.helpdesk.ui.screen.user.home

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.repository.MessageUpdatesRepository
import com.enoch02.helpdesk.data.remote.model.Chat
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.cloud_storage.CloudStorageRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentHomeViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val cloudStorageRepository: CloudStorageRepository,
    private val firestoreRepository: FirestoreRepository,
    private val messageUpdatesRepository: MessageUpdatesRepository
) :
    ViewModel() {
    var profilePicture by mutableStateOf<Uri?>(null)
    var userData by mutableStateOf(UserData(displayName = null))
    var chats by mutableStateOf(emptyList<Chat>())
    var allChatsData by mutableStateOf(emptyList<ChatsData>())

    init {
        messageUpdatesRepository.checkForUpdates()
    }

    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getUserData(firebaseAuthRepository.getUID())
                .onSuccess {
                    if (it != null) {
                        userData = it
                    }
                }
        }
    }

    fun getProfilePicture() {
        viewModelScope.launch(Dispatchers.IO) {
            cloudStorageRepository.getProfilePicture(firebaseAuthRepository.getUID())
                .onSuccess {
                    profilePicture = it
                }
        }
    }

    fun signOut() = firebaseAuthRepository.signOut()

    fun getChats() {
        viewModelScope.launch(Dispatchers.IO) {
            if (userData.userID != null) {
                firestoreRepository.getChats(userData.userID!!)
                    .onSuccess {
                        chats = it
                    }
                    .onFailure {
                        chats = emptyList()
                    }
            }
        }
    }

    fun getChatData() {
        viewModelScope.launch {
            val temp = mutableListOf<ChatsData>()

            chats.forEach { chat ->
                var name: String? = null
                var ticketSubject: String? = null
                var profilePic = Uri.EMPTY
                val mostRecentMessage: String? = chat.messages?.last()?.messageText
                var timeSent = chat.messages?.last()?.sentAt

                firestoreRepository.getUserName(chat.members?.staffID.toString())
                    .onSuccess {
                        name = it
                    }

                firestoreRepository.getTicket(
                    firebaseAuthRepository.getUID(),
                    chat.ticketID.toString()
                )
                    .onSuccess {
                        ticketSubject = it.subject
                    }

                cloudStorageRepository.getProfilePicture(chat.members?.staffID.toString())
                    .onSuccess {
                        profilePic = it
                    }

                temp.add(
                    ChatsData(
                        profilePic = profilePic,
                        name = name,
                        ticketSubject = ticketSubject,
                        mostRecentMessage = mostRecentMessage
                    )
                )
            }

            allChatsData = temp
        }
    }

    data class ChatsData(
        val profilePic: Uri?,
        val name: String?,
        val ticketSubject: String?,
        val mostRecentMessage: String?
    )
}
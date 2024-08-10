package com.enoch02.helpdesk.ui.screen.staff.home

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ChatsData
import com.enoch02.helpdesk.data.local.model.TicketStats
import com.enoch02.helpdesk.data.local.repository.MessageUpdatesRepository
import com.enoch02.helpdesk.data.remote.model.Chat
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.cloud_storage.CloudStorageRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StaffHomeViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val cloudStorageRepository: CloudStorageRepository,
    private val firestoreRepository: FirestoreRepository,
    private val messageUpdatesRepository: MessageUpdatesRepository,
) : ViewModel() {
    var profilePicture by mutableStateOf<Uri?>(null)
    var userData by mutableStateOf(UserData())
    var ticketStats by mutableStateOf(TicketStats())
    var chats by mutableStateOf(emptyList<Chat>())
    var allChatsData by mutableStateOf(emptyList<ChatsData>())

    var isRefreshing by mutableStateOf(false)

    init {
        getProfilePicture()
        getUserData()
        getStats()
        messageUpdatesRepository.checkForUpdates()
    }

    fun onRefresh() {
        isRefreshing = true
        getProfilePicture()
        getUserData()
        getStats()
        getChats()
        getChatData()
    }

    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getUserData(firebaseAuthRepository.getUID())
                .onSuccess {
                    if (it != null) {
                        withContext(Dispatchers.Main) {
                            userData = it
                            isRefreshing = false
                        }
                    }
                }
                .onFailure {
                    userData = UserData()
                    isRefreshing = false
                }
        }
    }

    fun getProfilePicture() {
        viewModelScope.launch(Dispatchers.IO) {
            cloudStorageRepository.getProfilePicture(firebaseAuthRepository.getUID())
                .onSuccess {
                    profilePicture = it
                    isRefreshing = false
                }
                .onFailure {
                    isRefreshing = false
                }
        }
    }

    private fun getStats() {
        viewModelScope.launch {
            firestoreRepository.getTicketStats()
                .onSuccess {
                    ticketStats = it
                    isRefreshing = false
                }
                .onFailure {
                    isRefreshing = false
                }
        }
    }

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

                firestoreRepository.getUserName(chat.members?.userID.toString())
                    .onSuccess {
                        name = it
                    }

                firestoreRepository.getTicket(
                    chat.members?.userID.toString(),
                    chat.ticketID.toString()
                )
                    .onSuccess {
                        ticketSubject = it.subject
                    }

                cloudStorageRepository.getProfilePicture(chat.members?.userID.toString())
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

    fun signOut() = firebaseAuthRepository.signOut()
}
package com.enoch02.helpdesk.ui.screen.staff.home

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.TicketStats
import com.enoch02.helpdesk.data.local.repository.MessageUpdatesRepository
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

    fun signOut() = firebaseAuthRepository.signOut()
}
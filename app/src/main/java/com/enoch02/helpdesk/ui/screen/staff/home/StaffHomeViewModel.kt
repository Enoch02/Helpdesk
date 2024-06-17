package com.enoch02.helpdesk.ui.screen.staff.home

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.TicketStats
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.cloud_storage.CloudStorageRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaffHomeViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val cloudStorageRepository: CloudStorageRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    var profilePicture by mutableStateOf<Uri?>(null)
    var userData by mutableStateOf(UserData(displayName = null))
    var ticketStats by mutableStateOf(TicketStats())

    init {
        getProfilePicture()
        getUserData()
        getStats()
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

    fun getStats() {
        viewModelScope.launch {
            firestoreRepository.getTicketStats()
                .onSuccess {
                    ticketStats = it
                }
        }
    }
}
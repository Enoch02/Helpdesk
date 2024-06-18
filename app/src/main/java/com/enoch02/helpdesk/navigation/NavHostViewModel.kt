package com.enoch02.helpdesk.navigation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavHostViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {
    var homeScreenContentState by mutableStateOf(ContentState.LOADING)
    var userData by mutableStateOf<UserData?>(null)

    init {
        getUserData()
    }

    fun isUserLoggedIn() = authRepository.isUserLoggedIn()

    private fun getUserData() {
        viewModelScope.launch {
            homeScreenContentState = ContentState.LOADING
            firestoreRepository.getUserData(uid = authRepository.getUID())
                .onSuccess {
                    userData = it
                    homeScreenContentState = ContentState.COMPLETED
                    Log.e("TAG", "getUserData: $userData", )
                }
        }
    }
}
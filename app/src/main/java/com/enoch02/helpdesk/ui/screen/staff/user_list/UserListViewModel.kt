package com.enoch02.helpdesk.ui.screen.staff.user_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.cloud_storage.CloudStorageRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val cloudStorageRepository: CloudStorageRepository,
    private val firestoreRepository: FirestoreRepository,
) :
    ViewModel() {
    var contentState by mutableStateOf(ContentState.LOADING)
    var errorMessage by mutableStateOf("")
    var users by mutableStateOf(listOf<UserData>())
    var query by mutableStateOf("")
    var searchActive by mutableStateOf(false)
    var searchResult = mutableStateListOf<UserData>()

    var isRefreshing by mutableStateOf(false)

    fun onRefresh() {
        isRefreshing = true
        getUsers()
    }

    fun clearQuery() {
        query = ""
    }

    fun updateQuery(newQuery: String) {
        query = newQuery
    }

    fun updateSearchStatus(newStatus: Boolean) {
        searchActive = newStatus
    }

    fun startSearch() {
        if (searchResult.isNotEmpty()) {
            searchResult.clear()
        }
        val temp = users.filter {
            it.displayName?.lowercase()?.contains(query.lowercase()) ?: false
        }

        searchResult.addAll(temp)
    }

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getUsers()
                .onSuccess {
                    users = it
                    contentState = ContentState.COMPLETED
                    isRefreshing = false
                }
                .onFailure {
                    errorMessage = it.message.toString()
                    contentState = ContentState.FAILURE
                    isRefreshing = false
                }
        }
    }
}
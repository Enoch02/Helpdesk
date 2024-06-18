package com.enoch02.helpdesk.ui.screen.common.knowledge_base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KnowledgeBaseViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) : ViewModel() {
    var contentState by mutableStateOf(ContentState.LOADING)
    var errorMessage by mutableStateOf("")
    var articles by mutableStateOf(listOf<String>())  // TODO: string for now
    var query by mutableStateOf("")
    var searchActive by mutableStateOf(false)
    var searchResult = mutableStateListOf<UserData>()

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
        //TODO:
        /*val temp = articles.filter {
            it.title?.lowercase()?.contains(query.lowercase()) ?: false
        }

        searchResult.addAll(temp)*/
    }

    fun getArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            /*firestoreRepository.()
                .onSuccess {

                    contentState = ContentState.COMPLETED
                }
                .onFailure {
                    errorMessage = it.message.toString()
                    contentState = ContentState.FAILURE
                }*/
        }
    }
}
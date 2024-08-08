package com.enoch02.helpdesk.ui.screen.common.knowledge_base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.model.Article
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KnowledgeBaseViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val authRepository: FirebaseAuthRepository
) :
    ViewModel() {
    var contentState by mutableStateOf(ContentState.LOADING)
    var errorMessage by mutableStateOf("")
    var articles by mutableStateOf(listOf<Article>())
    var query by mutableStateOf("")
    var searchActive by mutableStateOf(false)
    var searchResult = mutableStateListOf<Article>()
    var showBottomSheet by mutableStateOf(false)
    var role by mutableStateOf("")

    init {
        getUserRole()
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

        val temp = articles.filter {
            it.title?.lowercase()?.contains(query.lowercase()) ?: false
        }

        searchResult.addAll(temp)
    }

    fun getArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getArticles()
                .onSuccess {
                    articles = it
                    contentState = ContentState.COMPLETED
                }
                .onFailure {
                    errorMessage = it.message.toString()
                    contentState = ContentState.FAILURE
                }
        }
    }

    fun createArticle(title: String, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.createArticle(
                Article(authorId = authRepository.getUID(), title = title, content = content)
            )
                .onSuccess {
                    showBottomSheet = false
                }
                .onFailure {
                    showBottomSheet = false
                }
        }
    }

    private fun getUserRole() {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getUserData(authRepository.getUID())
                .onSuccess {
                    role = it?.role.toString()
                }
        }
    }

    suspend fun getUserNameFrom(id: String) = firestoreRepository.getUserName(id)
}
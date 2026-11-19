package com.example.vocabularylearningapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VocabRepository
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = VocabRepository(database.vocabDao())

        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.populateDatabaseFromAssets(application, database.vocabDao())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<Vocabulary>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.allVocabularies
            } else {
                repository.searchVocabularies(query.trim())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }
}

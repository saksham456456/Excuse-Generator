package com.example.savageexcuse

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.savageexcuse.data.Excuse
import com.example.savageexcuse.data.ExcuseDatabase
import com.example.savageexcuse.data.ExcuseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExcuseRepository
    private val _excuses = MutableStateFlow<List<Excuse>>(emptyList())
    val excuses: StateFlow<List<Excuse>> = _excuses

    private val _currentCategories = MutableStateFlow<List<String>>(emptyList())

    init {
        val excuseDao = ExcuseDatabase.getDatabase(application).excuseDao()
        repository = ExcuseRepository(excuseDao, application)
        viewModelScope.launch {
            repository.populateDatabaseIfEmpty()
            loadExcuses()
        }
    }

    fun loadExcuses() {
        viewModelScope.launch {
            if (_currentCategories.value.isEmpty()) {
                val all = repository.allExcuses.first()
                _excuses.value = all.shuffled()
            } else {
                val filtered = repository.getExcusesByCategories(_currentCategories.value).first()
                _excuses.value = filtered.shuffled()
            }
        }
    }

    fun setFilterCategories(categories: List<String>) {
        _currentCategories.value = categories
        loadExcuses()
    }

    fun toggleFavorite(excuse: Excuse) {
        viewModelScope.launch {
            repository.toggleFavorite(excuse)

            // Only update the specific item in the current state to prevent reshuffling
            val updatedList = _excuses.value.map { currentExcuse ->
                if (currentExcuse.id == excuse.id) {
                    currentExcuse.copy(isFavorite = !currentExcuse.isFavorite)
                } else {
                    currentExcuse
                }
            }
            _excuses.value = updatedList
        }
    }
}

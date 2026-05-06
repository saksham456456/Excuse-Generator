package com.example.savageexcuse

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.savageexcuse.data.Excuse
import com.example.savageexcuse.data.ExcuseDatabase
import com.example.savageexcuse.data.ExcuseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StashViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExcuseRepository

    val favoriteExcuses: StateFlow<List<Excuse>>

    init {
        val excuseDao = ExcuseDatabase.getDatabase(application).excuseDao()
        repository = ExcuseRepository(excuseDao, application)

        favoriteExcuses = repository.favoriteExcuses.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun removeFavorite(excuse: Excuse) {
        viewModelScope.launch {
            repository.toggleFavorite(excuse)
        }
    }
}

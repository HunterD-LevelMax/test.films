package com.euphoria.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.euphoria.app.data.models.Film
import com.euphoria.app.data.repository.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MoviesUiState>(MoviesUiState.Loading)
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = MoviesUiState.Loading
            try {
                val movies = repository.getMovies()
                _uiState.value = MoviesUiState.Success(movies)
            } catch (e: Exception) {
                _uiState.value = MoviesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class MoviesUiState {
        object Loading : MoviesUiState()
        data class Success(val movies: List<Film>) : MoviesUiState()
        data class Error(val message: String) : MoviesUiState()
    }
}
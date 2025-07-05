package com.euphoria.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.euphoria.app.data.models.Film
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GenresViewModel() : ViewModel() {

    private val _allMovies = MutableStateFlow<List<Film>>(emptyList())
    private val _filteredMovies = MutableStateFlow<List<Film>>(emptyList())
    val filteredMovies: StateFlow<List<Film>> = _filteredMovies.asStateFlow()

    private val _genresUiState = MutableStateFlow<GenresUiState>(GenresUiState.Success(emptyList()))
    val genresUiState: StateFlow<GenresUiState> = _genresUiState.asStateFlow()

    val selectedGenre = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            selectedGenre.collect {
                applyFilter()
            }
        }
    }

    fun setMovies(movies: List<Film>) {
        _allMovies.value = movies
        applyFilter()
    }

    fun filterMoviesByGenre(genre: String?) {
        selectedGenre.value = genre
    }

    private fun applyFilter() {
        val movies = _allMovies.value
        val genre = selectedGenre.value

        val filtered = if (genre.isNullOrBlank()) {
            movies
        } else {
            movies.filter { it.genres.any { g -> g.equals(genre, ignoreCase = true) } }
        }

        _filteredMovies.value = filtered

        val genres = movies.flatMap { it.genres }
            .distinct()
            .sorted()

        _genresUiState.value = GenresUiState.Success(genres)
    }

    sealed class GenresUiState {
        data class Success(val genres: List<String>) : GenresUiState()
    }
}

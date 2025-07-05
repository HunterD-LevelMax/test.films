package com.euphoria.app.data.repository

import com.euphoria.app.data.api.MoviesApi
import com.euphoria.app.data.models.Film

class MoviesRepository(private val api: MoviesApi) {
    suspend fun getMovies(): List<Film> {
        val response = api.getMovies()
        return response.films.sortedBy { it.localizedName }
    }
}

package com.euphoria.app.data.api

import com.euphoria.app.data.models.ApiResponse
import retrofit2.http.GET

interface MoviesApi {
    @GET("films.json")
    suspend fun getMovies(): ApiResponse
}
package com.euphoria.app.data.models

import com.squareup.moshi.Json

data class ApiResponse(
    @Json(name = "films") val films: List<Film>
)
package com.euphoria.app.data.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(
    @Json(name = "id") val id: Int,
    @Json(name = "localized_name") val localizedName: String,
    @Json(name = "name") val originalName: String,
    @Json(name = "year") val year: Int,
    @Json(name = "rating") val rating: Float?,
    @Json(name = "image_url") val imageUrl: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "genres") val genres: List<String>
) : Parcelable
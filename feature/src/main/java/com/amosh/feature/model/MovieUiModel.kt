package com.amosh.feature.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MovieUiModel(
    val id: Int,
    val adult: Boolean,
    val backdrop_path: String,
    val budget: Int,
    val genres: Int,
    val homepage: String,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: String,
    val production_countries: String,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: String,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
) : Parcelable
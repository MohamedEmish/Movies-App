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
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MovieUiModel

        if (id != other.id) return false
        if (imdb_id != other.imdb_id) return false
        if (original_title != other.original_title) return false
        if (poster_path != other.poster_path) return false
        if (title != other.title) return false
        if (vote_average != other.vote_average) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + adult.hashCode()
        result = 31 * result + backdrop_path.hashCode()
        result = 31 * result + budget
        result = 31 * result + genres
        result = 31 * result + homepage.hashCode()
        result = 31 * result + imdb_id.hashCode()
        result = 31 * result + original_language.hashCode()
        result = 31 * result + original_title.hashCode()
        result = 31 * result + overview.hashCode()
        result = 31 * result + popularity.hashCode()
        result = 31 * result + poster_path.hashCode()
        result = 31 * result + production_companies.hashCode()
        result = 31 * result + production_countries.hashCode()
        result = 31 * result + release_date.hashCode()
        result = 31 * result + revenue
        result = 31 * result + runtime
        result = 31 * result + spoken_languages.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + tagline.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + video.hashCode()
        result = 31 * result + vote_average.hashCode()
        result = 31 * result + vote_count
        return result
    }
}
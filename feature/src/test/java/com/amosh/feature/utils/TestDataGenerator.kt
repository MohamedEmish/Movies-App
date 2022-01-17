package com.amosh.feature.utils

import com.amosh.domain.entity.MovieEntity

class TestDataGenerator {

    companion object {
        fun generateMovieItem(): MovieEntity {
            return MovieEntity(
                adult = false,
                backdrop_path = "",
                budget = 20,
                genres = 1,
                homepage = "null",
                id = 1,
                imdb_id = "1",
                original_language = "en",
                original_title = "spider man",
                overview = "spider man runs away and comes back",
                popularity = 7.8,
                poster_path = "null",
                production_companies = "",
                production_countries = "",
                release_date = "5/5/1995",
                revenue = 100,
                runtime = 100,
                spoken_languages = "",
                status = "",
                tagline = "",
                title = "spider man",
                video = false,
                vote_average = 4.3,
                vote_count = 5,
            )
        }
    }
}
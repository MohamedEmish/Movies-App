package com.amosh.domain.utils

import com.amosh.domain.entity.MovieEntity

class TestDataGenerator {
    companion object {
        fun generateMoviesItems(): List<MovieEntity> {
            val item1 = MovieEntity(
                adult = false,
                backdrop_path = "",
                budget = 20,
                genres = 1,
                homepage = "null",
                id = 1,
                imdb_id = "1",
                original_language = "en",
                original_title = "spider man 1",
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
                title = "spider man 1",
                video = false,
                vote_average = 4.3,
                vote_count = 5,
            )

            val item2 = MovieEntity(
                adult = false,
                backdrop_path = "",
                budget = 20,
                genres = 1,
                homepage = "null",
                id = 1,
                imdb_id = "1",
                original_language = "en",
                original_title = "spider man 2",
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
                title = "spider man 2",
                video = false,
                vote_average = 4.3,
                vote_count = 5,
            )
            val item3 = MovieEntity(
                adult = false,
                backdrop_path = "",
                budget = 20,
                genres = 1,
                homepage = "null",
                id = 1,
                imdb_id = "1",
                original_language = "en",
                original_title = "spider man 3",
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
                title = "spider man 3",
                video = false,
                vote_average = 4.3,
                vote_count = 5,
            )
            return listOf(item1, item2, item3)
        }

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
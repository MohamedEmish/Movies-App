package com.amosh.remote.utils

import com.amosh.remote.model.*


/**
 * Dummy data generator for tests
 */
class TestDataGenerator {

    companion object {
        fun generateMovie(): ResultWrapper<List<MovieNetworkResponse>> {

            val resultObject = MovieNetworkResponse(
                adult = false,
                backdrop_path = "",
                belongs_to_collection = "",
                budget = 20,
                genres = listOf(1),
                homepage = "null",
                id = 1,
                imdb_id = "1",
                original_language = "en",
                original_title = "spider man",
                overview = "spider man runs away and comes back",
                popularity = 7.8,
                poster_path = "null",
                production_companies = listOf(ProductionCompany()),
                production_countries = listOf(ProductionCountry()),
                release_date = "5/5/1995",
                revenue = 100,
                runtime = 100,
                spoken_languages = listOf(SpokenLanguage()),
                status = "",
                tagline = "",
                title = "spider man",
                video = false,
                vote_average = 4.3,
                vote_count = 5,
            )

            val list = listOf(resultObject)
            return ResultWrapper(1, list)
        }
    }

}
package com.amosh.remote.mapper

import com.amosh.common.Mapper
import com.amosh.data.model.MovieDTO
import com.amosh.remote.model.MovieNetworkResponse
import javax.inject.Inject

class MovieNetworkDataMapper @Inject constructor() :
    Mapper<MovieNetworkResponse, MovieDTO> {

    override fun from(i: MovieNetworkResponse?): MovieDTO {
        return MovieDTO(
            id = i?.id ?: 0,
            adult = i?.adult ?: false,
            backdrop_path = i?.backdrop_path ?: "",
            budget = i?.budget ?: 0,
            genres = i?.genres?.get(0) ?: 0,
            homepage = i?.homepage ?: "",
            imdb_id = i?.imdb_id ?: "",
            original_language = i?.original_language ?: "",
            original_title = i?.original_title ?: "",
            overview = i?.overview ?: "",
            popularity = i?.popularity ?: 0.0,
            production_companies = i?.production_companies?.get(0)?.name ?: "",
            production_countries = i?.production_countries?.get(0)?.name ?: "",
            release_date = i?.release_date ?: "",
            revenue = i?.revenue ?: 0,
            runtime = i?.runtime ?: 0,
            spoken_languages = i?.spoken_languages?.get(0)?.name ?: "",
            status = i?.status ?: "",
            tagline = i?.tagline ?: "",
            title = i?.title ?: "",
            video = i?.video ?: false,
            vote_average = i?.vote_average ?: 0.0,
            vote_count = i?.vote_count ?: 0,
            poster_path = i?.poster_path ?: "",
            isFavorite = false
        )
    }

    override fun to(o: MovieDTO?): MovieNetworkResponse {
        return MovieNetworkResponse()
    }
}
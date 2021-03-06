package com.amosh.feature.mapper

import com.amosh.common.Mapper
import com.amosh.domain.entity.MovieEntity
import com.amosh.feature.model.MovieUiModel
import javax.inject.Inject

class MovieDomainUiMapper @Inject constructor() : Mapper<MovieEntity, MovieUiModel> {

    override fun from(i: MovieEntity?): MovieUiModel {
        return MovieUiModel(
            id = i?.id ?: 0,
            adult = i?.adult ?: false,
            backdrop_path = i?.backdrop_path ?: "",
            budget = i?.budget ?: 0,
            genres = i?.genres ?: 0,
            homepage = i?.homepage ?: "",
            imdb_id = i?.imdb_id ?: "",
            original_language = i?.original_language ?: "",
            original_title = i?.original_title ?: "",
            overview = i?.overview ?: "",
            popularity = i?.popularity ?: 0.0,
            poster_path = i?.poster_path ?: "",
            production_companies = i?.production_companies ?: "",
            production_countries = i?.production_countries ?: "",
            release_date = i?.release_date ?: "",
            revenue = i?.revenue ?: 0,
            runtime = i?.runtime ?: 0,
            spoken_languages = i?.spoken_languages ?: "",
            status = i?.status ?: "",
            tagline = i?.tagline ?: "",
            title = i?.title ?: "",
            video = i?.video ?: false,
            vote_average = i?.vote_average ?: 0.0,
            vote_count = i?.vote_count ?: 0,
        )
    }

    override fun to(o: MovieUiModel?): MovieEntity {
        return MovieEntity(
            id = o?.id ?: 0,
            adult = o?.adult ?: false,
            backdrop_path = o?.backdrop_path ?: "",
            budget = o?.budget ?: 0,
            genres = o?.genres ?: 0,
            homepage = o?.homepage ?: "",
            imdb_id = o?.imdb_id ?: "",
            original_language = o?.original_language ?: "",
            original_title = o?.original_title ?: "",
            overview = o?.overview ?: "",
            popularity = o?.popularity ?: 0.0,
            poster_path = o?.poster_path ?: "",
            production_companies = o?.production_companies ?: "",
            production_countries = o?.production_countries ?: "",
            release_date = o?.release_date ?: "",
            revenue = o?.revenue ?: 0,
            runtime = o?.runtime ?: 0,
            spoken_languages = o?.spoken_languages ?: "",
            status = o?.status ?: "",
            tagline = o?.tagline ?: "",
            title = o?.title ?: "",
            video = o?.video ?: false,
            vote_average = o?.vote_average ?: 0.0,
            vote_count = o?.vote_count ?: 0,
        )
    }

}
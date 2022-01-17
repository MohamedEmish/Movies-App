package com.amosh.moviesapp.di

import com.amosh.common.Mapper
import com.amosh.data.mapper.MovieDataDomainMapper
import com.amosh.data.model.MovieDTO
import com.amosh.domain.entity.MovieEntity
import com.amosh.feature.mapper.MovieDomainUiMapper
import com.amosh.feature.model.MovieUiModel
import com.amosh.local.mapper.MovieLocalDataMapper
import com.amosh.local.model.MovieLocalModel
import com.amosh.remote.mapper.MovieNetworkDataMapper
import com.amosh.remote.model.MovieNetworkResponse
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Module that holds Mappers
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {

    //region Locale Mappers
    @Binds
    abstract fun bindsMovieLocalDataMapper(mapper : MovieLocalDataMapper) : Mapper<MovieLocalModel, MovieDTO>
    //endregion

    //region Data Mappers
    @Binds
    abstract fun bindsMovieDataDomainMapper(mapper : MovieDataDomainMapper) : Mapper<MovieDTO, MovieEntity>
    //endregion

    //region Presentation Mappers
    @Binds
    abstract fun bindsMovieDomainUiMapper(mapper : MovieDomainUiMapper) : Mapper<MovieEntity, MovieUiModel>
    //endregion

    //region Remote Mappers
    @Binds
    abstract fun bindsMovieNetworkDataMapper(mapper: MovieNetworkDataMapper): Mapper<MovieNetworkResponse, MovieDTO>
    //endregion

}
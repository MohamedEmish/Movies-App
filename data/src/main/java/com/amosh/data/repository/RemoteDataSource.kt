package com.amosh.data.repository

import com.amosh.data.model.MovieDTO

/**
 * Methods of Remote Data Source
 */
interface RemoteDataSource {
    suspend fun getMoviesList(page: Int): List<MovieDTO>
}
package com.amosh.domain.repository

import com.amosh.common.Resource
import com.amosh.domain.entity.ListType
import com.amosh.domain.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

/**
 * Methods of Repository
 */
interface Repository {
    suspend fun getMoviesList(type: ListType): Flow<Resource<List<MovieEntity>>>
}
package com.amosh.domain.useCase

import com.amosh.common.Resource
import com.amosh.domain.entity.MovieEntity
import com.amosh.domain.qualifiers.IoDispatcher
import com.amosh.domain.repository.Repository
import com.amosh.domain.useCase.core.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddToFavoriteUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseUseCase<Long, MovieEntity>() {

    override suspend fun buildDetailsRequest(
        params: MovieEntity?
    ): Flow<Resource<Long>> {
        if (params == null) {
            return flow {
                emit(Resource.Error(Exception("movie can not be null")))
            }.flowOn(dispatcher)
        }
        return repository.addMovieToFavorite(
            params
        ).flowOn(dispatcher)
    }
}
package com.amosh.domain.useCase

import com.amosh.common.Constants
import com.amosh.common.Resource
import com.amosh.domain.entity.ListType
import com.amosh.domain.entity.MovieEntity
import com.amosh.domain.qualifiers.IoDispatcher
import com.amosh.domain.repository.Repository
import com.amosh.domain.useCase.core.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMoviesListUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseUseCase<List<MovieEntity>, Map<String, String>>() {

    override suspend fun buildDetailsRequest(params: Map<String, String>?): Flow<Resource<List<MovieEntity>>> {
        if (params == null) {
            return flow {
                emit(Resource.Error(Exception("id can not be null")))
            }.flowOn(dispatcher)
        }
        return if ((params[Constants.PAGE] ?: "1").toIntOrNull() ?: 1 < 500)
            repository.getMoviesList(
                type = ListType.valueOf(
                    params[Constants.TYPE] ?: ListType.ALL.name
                ),
                page = (params[Constants.PAGE] ?: "1").toIntOrNull() ?: 1
            ).flowOn(dispatcher)
        else flow { emit(Resource.Success(listOf())) }
    }
}
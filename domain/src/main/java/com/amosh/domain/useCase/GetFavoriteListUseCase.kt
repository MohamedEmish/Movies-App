package com.amosh.domain.useCase

import com.amosh.common.Resource
import com.amosh.domain.entity.ListType
import com.amosh.domain.entity.MovieEntity
import com.amosh.domain.qualifiers.IoDispatcher
import com.amosh.domain.repository.Repository
import com.amosh.domain.useCase.core.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFavoriteListUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseUseCase<List<MovieEntity>, ListType>() {

    override suspend fun buildDetailsRequest(type: ListType?): Flow<Resource<List<MovieEntity>>> {
        return repository.getMoviesList(ListType.FAVORITES).flowOn(dispatcher)
    }
}
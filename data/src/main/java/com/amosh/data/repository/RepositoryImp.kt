package com.amosh.data.repository

import android.util.Log
import com.amosh.common.Mapper
import com.amosh.common.Resource
import com.amosh.data.model.MovieDTO
import com.amosh.domain.entity.ListType
import com.amosh.domain.entity.MovieEntity
import com.amosh.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation class of [Repository]
 */
class RepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val movieMapper: Mapper<MovieDTO, MovieEntity>,
) : Repository {

    override suspend fun getMoviesList(
        type: ListType
    ): Flow<Resource<List<MovieEntity>>> {
        return flow {
            when (type) {
                ListType.FAVORITES -> {
                    try {
                        // Get data from LocalDataSource
                        val local = localDataSource.getFavoriteMoviesItems()
                        // Emit data
                        val resultList: MutableList<MovieEntity> = mutableListOf()
                        local.forEach {
                            resultList.add(movieMapper.from(it))
                        }
                        emit(Resource.Success(resultList))
                    } catch (ex1: Exception) {
                        // Emit error
                        emit(Resource.Error(ex1))
                    }
                }

                ListType.ALL -> {
                    try {
                        // Get data from RemoteDataSource
                        val data = remoteDataSource.getMoviesList()
                        // Save to local or update if exist
                        localDataSource.addItems(data)
                        // Emit data
                        val resultList: MutableList<MovieEntity> = mutableListOf()
                        data.forEach {
                            resultList.add(movieMapper.from(it))
                        }
                        emit(Resource.Success(resultList))
                    } catch (ex: Exception) {
                        // If remote request fails
                        try {
                            // Get data from LocalDataSource
                            val local = localDataSource.getItems()
                            // Emit data
                            val resultList: MutableList<MovieEntity> = mutableListOf()
                            local.forEach {
                                resultList.add(movieMapper.from(it))
                            }
                            emit(Resource.Success(resultList))
                        } catch (ex1: Exception) {
                            // Emit error
                            emit(Resource.Error(ex1))
                        }
                    }
                }
            }
        }
    }
}
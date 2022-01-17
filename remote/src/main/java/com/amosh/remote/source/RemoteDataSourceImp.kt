package com.amosh.remote.source

import com.amosh.common.Mapper
import com.amosh.data.model.MovieDTO
import com.amosh.data.repository.RemoteDataSource
import com.amosh.remote.api.ApiService
import com.amosh.remote.model.MovieNetworkResponse
import javax.inject.Inject


class RemoteDataSourceImp @Inject constructor(
    private val apiService: ApiService,
    private val movieMapper: Mapper<MovieNetworkResponse, MovieDTO>,
) : RemoteDataSource {

    override suspend fun getMoviesList(page: Int): List<MovieDTO> {
        val networkData = apiService.getMovieList(page)
        val moviesList: MutableList<MovieDTO> = mutableListOf()
        networkData.results?.forEach {
            moviesList.add(movieMapper.from(it))
        }
        return moviesList
    }
}
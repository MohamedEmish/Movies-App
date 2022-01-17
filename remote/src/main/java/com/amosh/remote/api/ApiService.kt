package com.amosh.remote.api

import com.amosh.remote.BuildConfig
import com.amosh.remote.model.MovieNetworkResponse
import com.amosh.remote.model.ResultWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Query("movie_id") movieId: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): MovieNetworkResponse

    @GET("discover/movie")
    suspend fun getMovieList(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): ResultWrapper<List<MovieNetworkResponse>>
}
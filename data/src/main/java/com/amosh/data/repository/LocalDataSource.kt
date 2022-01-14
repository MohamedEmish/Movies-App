package com.amosh.data.repository

import com.amosh.data.model.MovieDTO


/**
 * Methods of Local Data Source
 */
interface LocalDataSource {

    suspend fun addItem(movie: MovieDTO): Long

    suspend fun getItemByTitle(title: String): MovieDTO

    suspend fun getItemById(movieId: String): MovieDTO

    suspend fun addItems(movies: List<MovieDTO>): List<Long>

    suspend fun getItems(): List<MovieDTO>

    suspend fun getFavoriteMoviesItems(): List<MovieDTO>

    suspend fun updateItem(movie: MovieDTO): Int

    suspend fun deleteItemById(id: Int): Int

    suspend fun deleteItem(movie: MovieDTO): Int

    suspend fun clearCachedItems(): Int
}
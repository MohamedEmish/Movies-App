package com.amosh.local.database

import androidx.room.*
import com.amosh.local.model.MovieLocalModel

@Dao
interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovieItem(movie: MovieLocalModel): Long

    @Query("SELECT * FROM movies WHERE title = :title")
    suspend fun getMovieItemByTitle(title: String): MovieLocalModel

    @Query("SELECT * FROM movies WHERE imdb_id = :id")
    suspend fun getMovieItemById(id: String): MovieLocalModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovieItems(movies: List<MovieLocalModel>): List<Long>

    @Query("SELECT * FROM movies")
    suspend fun getMoviesItems(): List<MovieLocalModel>

    @Query("SELECT * FROM movies WHERE isFavorite =:isFav ")
    suspend fun getFavoriteMoviesItems(isFav: Boolean = true): List<MovieLocalModel>

    @Update
    suspend fun updateMovieItem(movie: MovieLocalModel): Int

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun deleteMovieItemById(id: Int): Int

    @Delete
    suspend fun deleteMovieItem(movie: MovieLocalModel): Int

    @Query("DELETE FROM movies")
    suspend fun clearCachedMovieItems(): Int
}
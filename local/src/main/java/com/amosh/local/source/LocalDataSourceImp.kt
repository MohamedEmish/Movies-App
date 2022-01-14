package com.amosh.local.source

import com.amosh.common.Mapper
import com.amosh.data.model.MovieDTO
import com.amosh.data.repository.LocalDataSource
import com.amosh.local.database.MoviesDAO
import com.amosh.local.model.MovieLocalModel
import javax.inject.Inject


/**
 * Implementation of [LocalDataSource] Source
 */
class LocalDataSourceImp @Inject constructor(
    private val moviesDAO: MoviesDAO,
    private val movieMapper: Mapper<MovieLocalModel, MovieDTO>
) : LocalDataSource {


    override suspend fun addItem(movie: MovieDTO): Long {
        val movieLocalModel = movieMapper.to(movie)
        return moviesDAO.addMovieItem(movie = movieLocalModel)
    }

    override suspend fun getItemByTitle(title: String): MovieDTO {
        val movieLocalModel = moviesDAO.getMovieItemByTitle(title = title)
        return movieMapper.from(movieLocalModel)
    }

    override suspend fun getItemById(movieId: String): MovieDTO {
        val movieLocalModel = moviesDAO.getMovieItemById(id = movieId)
        return movieMapper.from(movieLocalModel)
    }

    override suspend fun addItems(movies: List<MovieDTO>): List<Long> {
        val movieLocalList = movieMapper.toList(movies)
        return moviesDAO.addMovieItems(movies = movieLocalList)
    }

    override suspend fun getItems(): List<MovieDTO> {
        val movieLocalList = moviesDAO.getMoviesItems()
        return movieMapper.fromList(movieLocalList)
    }

    override suspend fun getFavoriteMoviesItems(): List<MovieDTO> {
        val movieLocalList = moviesDAO.getMoviesItems()
        return movieMapper.fromList(movieLocalList)
    }

    override suspend fun updateItem(movie: MovieDTO): Int {
        val movieLocalModel = movieMapper.to(movie)
        return moviesDAO.updateMovieItem(movie = movieLocalModel)
    }

    override suspend fun deleteItemById(id: Int): Int {
        return moviesDAO.deleteMovieItemById(id = id)
    }

    override suspend fun deleteItem(movie: MovieDTO): Int {
        val movieLocalModel = movieMapper.to(movie)
        return moviesDAO.deleteMovieItem(movie = movieLocalModel)
    }

    override suspend fun clearCachedItems(): Int {
        return moviesDAO.clearCachedMovieItems()
    }
}
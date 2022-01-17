package com.amosh.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.amosh.local.database.AppDatabase
import com.amosh.local.database.MoviesDAO
import com.amosh.local.utils.TestDataGenerator
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class MovieDAOTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase
    private lateinit var movieDao: MoviesDAO

    @Before
    fun setUp() {
        hiltRule.inject()
        movieDao = database.moviesDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun test_add_movie_item_success() = runBlockingTest {

        val item = TestDataGenerator.generateMovieItem()

        movieDao.addMovieItem(item)

        val items = movieDao.getMoviesItems()

        Truth.assertThat(items).contains(item)

    }

    @Test
    fun test_get_movie_item_success() = runBlockingTest {

        val item = TestDataGenerator.generateMovieItem()

        movieDao.addMovieItem(item)

        val result = movieDao.getMovieItemById("1")

        Truth.assertThat(item).isEqualTo(result)
    }

    @Test
    fun test_add_and_get_movie_items_success() = runBlockingTest {

        val item = TestDataGenerator.generateMovieItem()

        movieDao.addMovieItem(item)

        val result = movieDao.getMoviesItems()

        Truth.assertThat(result).containsExactly(item)
    }

    @Test
    fun test_update_movie_item_success() = runBlockingTest {

        val item = TestDataGenerator.generateMovieItem()

        movieDao.addMovieItem(item)

        val dbItem = movieDao.getMovieItemById(item.id.toString())

        Truth.assertThat(item).isEqualTo(dbItem)

        val updatedItem = item.copy(title = "updated name")

        movieDao.updateMovieItem(updatedItem)

        val result = movieDao.getMovieItemById(updatedItem.id.toString())

        Truth.assertThat(updatedItem).isEqualTo(result)

    }

    @Test
    fun test_delete_movie_item_by_id_success() = runBlockingTest {

        val item = TestDataGenerator.generateMovieItem()

        movieDao.addMovieItem(item)

        val dbItem = movieDao.getMovieItemById(item.id.toString())

        Truth.assertThat(item).isEqualTo(dbItem)

        movieDao.deleteMovieItem(item)

        val items = movieDao.getMoviesItems()

        Truth.assertThat(items).doesNotContain(item)

    }

    @Test
    fun test_delete_movie_item_success() = runBlockingTest {

        val item = TestDataGenerator.generateMovieItem()

        movieDao.addMovieItem(item)

        val dbItem = movieDao.getMovieItemById(item.id.toString())

        Truth.assertThat(item).isEqualTo(dbItem)

        movieDao.deleteMovieItem(item)

        val items = movieDao.getMoviesItems()

        Truth.assertThat(items).doesNotContain(item)

    }

    @Test
    fun test_clear_all_movies_success() = runBlockingTest {

        val items = TestDataGenerator.generateMoviesItems()

        movieDao.addMovieItems(items)

        val dbItems = movieDao.getMoviesItems()

        Truth.assertThat(dbItems).containsExactlyElementsIn(items)

        movieDao.clearCachedMovieItems()

        val result = movieDao.getMoviesItems()

        Truth.assertThat(result).isEmpty()
    }
}
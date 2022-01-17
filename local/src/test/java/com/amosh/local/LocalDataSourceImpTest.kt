package com.amosh.local

import androidx.test.filters.SmallTest
import com.amosh.data.repository.LocalDataSource
import com.amosh.local.database.MoviesDAO
import com.amosh.local.mapper.MovieLocalDataMapper
import com.amosh.local.source.LocalDataSourceImp
import com.amosh.local.utils.TestData
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@SmallTest
class LocalDataSourceImpTest {

    @MockK
    private lateinit var movieDao: MoviesDAO

    private val movieLocalDataMapper = MovieLocalDataMapper()
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create LocalDataSourceImp before every test
        localDataSource = LocalDataSourceImp(
            moviesDAO = movieDao,
            movieMapper = movieLocalDataMapper
        )
    }

    @Test
    fun test_add_movie_item_success() = runBlockingTest {

        val movieLocal = TestData.generateMovieItem()
        val expected = 1L

        // Given
        coEvery { movieDao.addMovieItem(any()) } returns expected

        // When
        val returned = localDataSource.addItem(movieLocalDataMapper.from(i = movieLocal))

        // Then
        coVerify { movieDao.addMovieItem(any()) }

        // Assertion
        Truth.assertThat(returned).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun test_add_movie_item_fail() = runBlockingTest {

        val movieItem = movieLocalDataMapper.from(i = TestData.generateMovieItem())

        // Given
        coEvery { movieDao.addMovieItem(any()) } throws Exception()

        // When
        localDataSource.addItem(movieItem)

        // Then
        coVerify { movieDao.addMovieItem(any()) }

    }

    @Test
    fun test_get_movie_item_success() = runBlockingTest {

        val movieLocal = TestData.generateMovieItem()
        val expected = movieLocalDataMapper.from(i = movieLocal)

        // Given
        coEvery { movieDao.getMovieItemById(any()) } returns movieLocal

        // When
        val returned = localDataSource.getItemById(movieLocal.id.toString())

        // Then
        coVerify { movieDao.getMovieItemById(any()) }

        // Assertion
        Truth.assertThat(returned).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun test_get_movie_item_fail() = runBlockingTest {

        val movieItem = movieLocalDataMapper.from(i = TestData.generateMovieItem())

        // Given
        coEvery { movieDao.getMovieItemById(any()) } throws Exception()

        // When
        localDataSource.getItemById(movieItem.id.toString())

        // Then
        coVerify { movieDao.getMovieItemById(any()) }

    }

    @Test
    fun test_add_movie_items_success() = runBlockingTest {

        val movieItems = movieLocalDataMapper.fromList(list = TestData.generateMoviesItems())
        val expected = MutableList(movieItems.size) { index -> index.toLong() }

        // Given
        coEvery { movieDao.addMovieItems(any()) } returns expected

        // When
        val returned = localDataSource.addItems(movieItems)

        // Then
        coVerify { movieDao.addMovieItems(any()) }

        // Assertion
        Truth.assertThat(returned).hasSize(expected.size)
    }

    @Test(expected = Exception::class)
    fun test_add_movie_items_fail() = runBlockingTest {

        val movieItems = movieLocalDataMapper.fromList(list = TestData.generateMoviesItems())

        // Given
        coEvery { movieDao.addMovieItems(any()) } throws Exception()

        // When
        localDataSource.addItems(movieItems)

        // Then
        coVerify { movieDao.addMovieItems(any()) }

    }

    @Test
    fun test_get_movie_items_success() = runBlockingTest {

        val movieItems = TestData.generateMoviesItems()
        val expected = movieLocalDataMapper.fromList(list = movieItems)

        // Given
        coEvery { movieDao.getMoviesItems() } returns movieItems

        // When
        val returned = localDataSource.getItems()

        // Then
        coVerify { movieDao.getMoviesItems() }

        // Assertion
        Truth.assertThat(returned).containsExactlyElementsIn(expected)
    }

    @Test(expected = Exception::class)
    fun test_get_movie_items_fail() = runBlockingTest {

        // Given
        coEvery { movieDao.getMoviesItems() } throws Exception()

        // When
        localDataSource.getItems()

        // Then
        coVerify { movieDao.getMoviesItems() }

    }

    @Test
    fun test_update_movie_item_success() = runBlockingTest {

        val movieItem = movieLocalDataMapper.from(i = TestData.generateMovieItem())
        val expected = 1

        // Given
        coEvery { movieDao.updateMovieItem(any()) } returns expected

        // When
        val returned = localDataSource.updateItem(movieItem)

        // Then
        coVerify { movieDao.updateMovieItem(any()) }

        // Assertion
        Truth.assertThat(returned).isEqualTo(expected)

    }

    @Test(expected = Exception::class)
    fun test_update_movie_item_fail() = runBlockingTest {

        val movieItem = movieLocalDataMapper.from(i = TestData.generateMovieItem())

        // Given
        coEvery { movieDao.updateMovieItem(any()) } throws Exception()

        // When
        localDataSource.updateItem(movieItem)

        // Then
        coVerify { movieDao.updateMovieItem(any()) }

    }

    @Test
    fun test_delete_movie_item_by_id_success() = runBlockingTest {

        val expected = 1

        // Given
        coEvery { movieDao.deleteMovieItemById(any()) } returns expected

        // When
        val returned = localDataSource.deleteItemById(1)

        // Then
        coVerify { movieDao.deleteMovieItemById(any()) }

        // Assertion
        Truth.assertThat(returned).isEqualTo(expected)

    }

    @Test(expected = Exception::class)
    fun test_delete_movie_item_by_id_fail() = runBlockingTest {

        // Given
        coEvery { movieDao.deleteMovieItemById(any()) } throws Exception()

        // When
        localDataSource.deleteItemById(1)

        // Then
        coVerify { movieDao.deleteMovieItemById(any()) }

    }

    @Test
    fun test_delete_movie_item_success() = runBlockingTest {

        val movieItem = movieLocalDataMapper.from(i = TestData.generateMovieItem())
        val expected = 1

        // Given
        coEvery { movieDao.deleteMovieItem(any()) } returns expected

        // When
        val returned = localDataSource.deleteItem(movieItem)

        // Then
        coVerify { movieDao.deleteMovieItem(any()) }

        // Assertion
        Truth.assertThat(returned).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun test_delete_movie_item_fail() = runBlockingTest {

        val movieItem = movieLocalDataMapper.from(i = TestData.generateMovieItem())

        // Given
        coEvery { movieDao.deleteMovieItem(any()) } throws Exception()

        // When
        localDataSource.deleteItem(movieItem)

        // Then
        coVerify { movieDao.deleteMovieItem(any()) }

    }

    @Test
    fun test_clear_all_movies_success() = runBlockingTest {

        val movieItems = movieLocalDataMapper.fromList(list = TestData.generateMoviesItems())
        val expected = movieItems.size // Affected row count

        // Given
        coEvery { movieDao.clearCachedMovieItems() } returns expected

        // When
        val returned = localDataSource.clearCachedItems()

        // Then
        coVerify { movieDao.clearCachedMovieItems() }

        // Assertion
        Truth.assertThat(returned).isEqualTo(expected)

    }

    @Test(expected = Exception::class)
    fun test_clear_all_movies_fail() = runBlockingTest {

        // Given
        coEvery { movieDao.clearCachedMovieItems() } throws Exception()

        // When
        localDataSource.clearCachedItems()

        // Then
        coVerify { movieDao.clearCachedMovieItems() }

    }
}
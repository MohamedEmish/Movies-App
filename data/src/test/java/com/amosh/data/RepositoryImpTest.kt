package com.amosh.data

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.amosh.common.Resource
import com.amosh.data.mapper.MovieDataDomainMapper
import com.amosh.data.model.MovieDTO
import com.amosh.data.repository.LocalDataSource
import com.amosh.data.repository.RemoteDataSource
import com.amosh.data.repository.RepositoryImp
import com.amosh.data.utils.TestDataGenerator
import com.amosh.domain.entity.ListType
import com.amosh.domain.repository.Repository
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@SmallTest
class RepositoryImpTest {

    @MockK
    private lateinit var localDataSource: LocalDataSource

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource

    private val movieMapper = MovieDataDomainMapper()

    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create RepositoryImp before every test
        repository = RepositoryImp(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            movieMapper = movieMapper
        )
    }

    @Test
    fun test_get_movies_remote_success() = runBlockingTest {

        val movieItem = TestDataGenerator.generateMovieItem()
        val affectedIds = 1

        // Given
        coEvery { remoteDataSource.getMoviesList(1)[0] } returns movieItem
        coEvery { localDataSource.addItem(movieItem) } returns affectedIds.toLong()
        coEvery { localDataSource.getItemById(any()) } returns MovieDTO(
            adult = false,
            backdrop_path = "",
            budget = 20,
            genres = 1,
            homepage = "null",
            id = 1,
            imdb_id = "1",
            original_language = "en",
            original_title = "spider man 1",
            overview = "spider man runs away and comes back",
            popularity = 7.8,
            poster_path = "null",
            production_companies = "",
            production_countries = "",
            release_date = "5/5/1995",
            revenue = 100,
            runtime = 100,
            spoken_languages = "",
            status = "",
            tagline = "",
            title = "spider man 1",
            video = false,
            vote_average = 4.3,
            vote_count = 5,
        )

        // When & Assertions
        val flow = repository.getMoviesList(ListType.ALL, 1)
        flow.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData[0].id).isEqualTo(movieMapper.from(movieItem).id)
            expectComplete()
        }

        // Then
        coVerify { remoteDataSource.getMoviesList(1) }
        coVerify { localDataSource.addItem(movieItem) }
    }

    @Test
    fun test_get_movies_remote_fail_local_success() = runBlockingTest {

        val movie = TestDataGenerator.generateMovieItem()

        // Given
        coEvery { remoteDataSource.getMoviesList(1) } throws Exception()
        coEvery { localDataSource.getItemById(any()) } returns movie

        // When && Assertions
        val flow = repository.getMoviesList(ListType.ALL, 1)
        flow.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData[0].id).isEqualTo(movieMapper.from(movie).id)
            expectComplete()
        }

        // Then
        coVerify { remoteDataSource.getMoviesList(1) }
        coVerify { localDataSource.getItemById(any()) }

    }

    @Test
    fun test_get_movies_remote_fail_local_fail() = runBlockingTest {


        // Given
        coEvery { remoteDataSource.getMoviesList(1) } throws Exception()
        coEvery { localDataSource.getItemById(any()) } throws Exception()

        // When && Assertions
        val flow = repository.getMoviesList(ListType.ALL, 1)
        flow.test {
            // Expect Resource.Error
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }

        // Then
        coVerify { remoteDataSource.getMoviesList(1) }
        coVerify { localDataSource.getItemById(any()) }

    }
}
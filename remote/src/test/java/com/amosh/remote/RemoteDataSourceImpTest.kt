package com.amosh.remote

import androidx.test.filters.SmallTest
import com.amosh.data.model.MovieDTO
import com.amosh.data.repository.RemoteDataSource
import com.amosh.remote.api.ApiService
import com.amosh.remote.mapper.MovieNetworkDataMapper
import com.amosh.remote.source.RemoteDataSourceImp
import com.amosh.remote.utils.TestDataGenerator
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
class RemoteDataSourceImpTest {

    @MockK
    private lateinit var apiService : ApiService
    private val movieNetworkDataMapper = MovieNetworkDataMapper()

    private lateinit var remoteDataSource : RemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create RemoteDataSourceImp before every test
        remoteDataSource = RemoteDataSourceImp(
            apiService = apiService,
            movieMapper = movieNetworkDataMapper,
        )
    }

    @Test
    fun test_get_Movies_success() = runBlockingTest {

        val movieNetwork = TestDataGenerator.generateMovie()

        // Given
        coEvery { apiService.getMovieList(any()) } returns movieNetwork

        // When
        val result = remoteDataSource.getMoviesList(1)

        // Then
        coVerify { apiService.getMovieList(any()) }

        // Assertion
        val expectedList : MutableList<MovieDTO> = mutableListOf()
        movieNetwork.results?.forEach {
            expectedList.add(movieNetworkDataMapper.from(it))
        }
        val expected = expectedList.toList()
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun test_get_Movies_fail() = runBlockingTest {

        // Given
        coEvery { apiService.getMovieList(any()) } throws Exception()

        // When
        remoteDataSource.getMoviesList(1)

        // Then
        coVerify { apiService.getMovieList(any()) }

    }
}
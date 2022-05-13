package com.codegrow.data.repository

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.codegrow.domain.core.MainCoroutineRule
import com.codegrow.domain.core.TestUtilities
import com.codegrow.domain.core.Resource
import com.codegrow.domain.repository.TransactionRepository
import com.codegrow.remote.service.ApiService
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime


@ExperimentalTime
@ExperimentalCoroutinesApi
@SmallTest
class TransactionRepositoryImpTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var apiService: ApiService

    private lateinit var repository: TransactionRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        // Create RepositoryImp before every test
        repository = TransactionRepositoryImp(
            apiService = apiService,
            dispatcher = mainCoroutineRule.dispatcher
        )
    }

    @Test
    fun test_convert_remote_success() = runBlockingTest {

        val conversionResponse = TestUtilities.getConvertTestObject()

        // Given
        coEvery { apiService.convert(any(),any(),any(),any()) } returns conversionResponse


        // When & Assertions
        val flow = repository.convert(0.0,"","")
        flow.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData).isEqualTo(conversionResponse.result)
            expectComplete()
        }

        // Then
        coVerify { apiService.convert(any(),any(),any(),any()) }
    }

    @Test
    fun test_convert_remote_fail() = runBlockingTest {


        val convertResponse = TestUtilities.getSymbolsTestObject()

        // Given
        coEvery { apiService.convert(any(),any(),any(),any()) } throws Exception()


        // When & Assertions
        val flow = repository.convert(0.0,"","")
        flow.test {
            // Expect Resource.Success
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }
        // Then
        coVerify { apiService.convert(any(),any(),any(),any()) }

    }





    @Test
    fun test_getHistorical_remote_success() = runBlockingTest {

        val historicalResponse = TestUtilities.getHistoricalTestObject()

        // Given
        coEvery { apiService.getHistoricalSearch(any(),any(),any()) } returns historicalResponse


        // When & Assertions
        val flow = repository.getHistorical("","")
        flow.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData.size).isEqualTo(historicalResponse.rates.size)
            expectComplete()
        }

        // Then
        coVerify { apiService.getHistoricalSearch(any(),any(),any()) }
    }



    @Test
    fun test_getHistorical_remote_fail() = runBlockingTest {


        val historicalResponse = TestUtilities.getHistoricalTestObject()

        // Given
        coEvery { apiService.getHistoricalSearch(any(),any(),any()) } throws Exception()


        // When & Assertions
        val flow = repository.getHistorical("","")
        flow.test {
            // Expect Resource.Success
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }
        // Then
        coVerify { apiService.getHistoricalSearch(any(),any(),any()) }

    }




}

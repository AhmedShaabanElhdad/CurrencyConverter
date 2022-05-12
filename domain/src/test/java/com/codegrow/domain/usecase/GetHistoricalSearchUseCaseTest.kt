package com.codegrow.domain.usecase

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.codegrow.domain.core.MainCoroutineRule
import com.codegrow.domain.core.Resource
import com.codegrow.domain.core.TestDataGenerator
import com.codegrow.domain.repository.TransactionRepository
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@SmallTest
class GetHistoricalSearchUseCaseTest{
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var repository: TransactionRepository

    private lateinit var getHistoricalSearchUseCase: GetHistoricalSearchUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        getHistoricalSearchUseCase = GetHistoricalSearchUseCase(
            repository = repository
        )
    }

    @Test
    fun test_get_historical_success() = runBlockingTest {

        val rates = TestDataGenerator.generateRateItems()
        val flow = flowOf(Resource.Success(rates))

        // Given
        coEvery { repository.getHistorical(any(),any()) } returns flow

        // When & Assertions
        val result = getHistoricalSearchUseCase.execute(HistoricalInput("2022-10-05","2022-10-05"))
        result.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = (expected as Resource.Success).data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData).isEqualTo(rates)
            expectComplete()
        }

        // Then
        coVerify { repository.getHistorical(any(),any()) }

    }




    @Test
    fun test_get_historical_fail() = runBlockingTest {

        val errorFlow = flowOf(Resource.Error("Error"))

        // Given
        coEvery { repository.getHistorical(any(),any()) } returns errorFlow

        // When & Assertions
        val result = getHistoricalSearchUseCase.execute(HistoricalInput("2022-10-05","2022-10-05"))
        result.test {
            // Expect Resource.Error
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }

        // Then
        coVerify { repository.getHistorical(any(),any()) }

    }
}
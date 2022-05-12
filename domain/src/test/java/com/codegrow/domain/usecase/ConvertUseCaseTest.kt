package com.codegrow.domain.usecase

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.codegrow.domain.core.MainCoroutineRule
import com.codegrow.domain.core.Resource
import com.codegrow.domain.core.TestDataGenerator
import com.codegrow.domain.repository.SymbolRepository
import com.codegrow.domain.repository.TransactionRepository
import com.google.common.truth.Truth
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@SmallTest
class ConvertUseCaseTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var repository: TransactionRepository

    private lateinit var convertUseCase: ConvertUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        convertUseCase = ConvertUseCase(
            repository = repository
        )
    }

    @Test
    fun test_convert_success() = runBlockingTest {

        val resultAmount = 10.0
        val flow = flowOf(Resource.Success(resultAmount))

        // Given
        coEvery { repository.convert(any(), any(), any()) } returns flow

        // When & Assertions
        val result = convertUseCase.execute(ConvertInput(10.0, "EAD", "EUR"))
        flow.test {
            // Expect Resource.Success
            val expected = expectItem()
            val expectedData = expected.data
            Truth.assertThat(expected).isInstanceOf(Resource.Success::class.java)
            Truth.assertThat(expectedData).isEqualTo(resultAmount)
            expectComplete()
        }

        // Then
        coVerify { repository.convert(any(), any(), any()) }

    }

    @Test
    fun test_convert_invalid_amount_fail() = runBlockingTest {

        val errorFlow = flowOf(Resource.Error("invalid amount"))

        // Given
        coEvery { repository.convert(any(), any(), any()) } returns errorFlow

        // When & Assertions
        val result = convertUseCase.execute(ConvertInput(null, "EAD", "EUR"))
        result.test {
            // Expect Resource.Success

            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }

        // Then
        coVerify { repository.convert(any(), any(), any()) wasNot Called }

    }




    @Test
    fun test_convert_invalid_from_fail() = runBlockingTest {

        val errorFlow = flowOf(Resource.Error("invalid symbol from"))

        // Given
        coEvery { repository.convert(any(), any(), "") } returns errorFlow

        // When & Assertions
        val result = convertUseCase.execute(ConvertInput(10.0, "", "EUR"))
        result.test {
            // Expect Resource.Success
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }

        // Then
        coVerify { repository.convert(any(),"",any()) wasNot Called }

    }

    @Test
    fun test_convert_invalid_to_fail() = runBlockingTest {

        val errorFlow = flowOf(Resource.Error("invalid symbol to"))

        // Given
        coEvery { repository.convert(any(), any(), "") } returns errorFlow

        // When & Assertions
        val result = convertUseCase.execute(ConvertInput(10.0, "EUR", ""))
        result.test {
            // Expect Resource.Success
            Truth.assertThat(expectItem()).isInstanceOf(Resource.Error::class.java)
            expectComplete()
        }

        // Then
        coVerify { repository.convert(any(), any(), "") wasNot Called }

    }



}
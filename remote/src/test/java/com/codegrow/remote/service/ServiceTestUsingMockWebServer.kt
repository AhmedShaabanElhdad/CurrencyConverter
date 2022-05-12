package com.codegrow.remote.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.codegrow.remote.MockWebServerBaseTest
import com.codegrow.remote.TestUtilities
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

const val symbolFileName = "symbols.json"
const val convertFileName = "convert.json"
const val historicalRateFileName = "HistoricalRate.json"
const val unauthorizedFileName = "unauthorized.json"

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ServiceTestUsingMockWebServer :MockWebServerBaseTest() {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var apiService: ApiService
    private val key = anyString()

    override fun isMockServerEnabled() = true

    @Before
    fun start() {
        apiService = provideTestApiService()
    }

    @Test
    fun `given response ok when fetching results then return a response with elements`() {
        runBlocking {
            mockHttpResponse(symbolFileName, HttpURLConnection.HTTP_OK)
            val apiResponse = apiService.getSymbol(key)

            assertNotNull(apiResponse)
            assertEquals(apiResponse.success, true)
        }
    }

    @Test
    fun `given response Error when fetching result then return an error`() {
        runBlocking {
            mockHttpResponse(unauthorizedFileName, HttpURLConnection.HTTP_NOT_AUTHORITATIVE)
            val apiResponse = apiService.getSymbol(key)

            assertNotNull(apiResponse)
            assertEquals(apiResponse.message, "No API key found in request")
        }
    }

}
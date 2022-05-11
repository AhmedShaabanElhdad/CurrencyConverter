package com.codegrow.remote.service


import com.codegrow.remote.resources.ConversionResponse
import com.codegrow.remote.resources.HistoricalResponse
import com.codegrow.remote.resources.RateResponse
import com.codegrow.remote.resources.SymbolResponse
import retrofit2.http.*


interface ApiService {

    @GET("symbols")
    suspend fun getSymbol(
        @Query("apikey") key: String = "H6hntPyzizCpVakStIJDbWJYu8fKWQor"
    ):SymbolResponse

    @GET("convert")
    suspend fun convert(
        @Query("amount") amount: Double,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("apikey") key: String = "H6hntPyzizCpVakStIJDbWJYu8fKWQor",
    ):ConversionResponse

    @GET("timeseries")
    suspend fun getHistoricalSearch(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("apikey") key: String = "H6hntPyzizCpVakStIJDbWJYu8fKWQor",
    ):HistoricalResponse

    @GET("{date}")
    suspend fun getHistoricalRate(
        @Path("date") date: String,
        @Query("symbols") symbols: String,
        @Query("base") base: String,
        @Query("apikey") key: String = "H6hntPyzizCpVakStIJDbWJYu8fKWQor",
    ): RateResponse

}
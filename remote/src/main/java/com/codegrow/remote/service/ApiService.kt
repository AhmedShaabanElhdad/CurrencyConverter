package com.codegrow.remote.service


import com.codegrow.remote.response.ConversionResponse
import com.codegrow.remote.response.HistoricalResponse
import com.codegrow.remote.response.RateResponse
import com.codegrow.remote.response.SymbolResponse
import retrofit2.http.*


interface ApiService {

    @GET("symbols")
    suspend fun getSymbol(
        @Query("apikey") key: String
    ):SymbolResponse

    @GET("convert")
    suspend fun convert(
        @Query("amount") amount: Double,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("apikey") key: String,
    ):ConversionResponse

    @GET("timeseries")
    suspend fun getHistoricalSearch(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("apikey") key: String,
    ):HistoricalResponse

    @GET("{date}")
    suspend fun getHistoricalRate(
        @Query("date") date: String,
        @Query("symbols") symbols: String,
        @Query("base") base: String,
        @Query("apikey") key: String,
    ): RateResponse

}
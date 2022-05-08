package com.codegrow.remote.response

import com.codegrow.remote.response.model.Rates

data class HistoricalResponse(
    val base: String,
    val end_date: String,
    val rates: Rates,
    val start_date: String,
    val success: Boolean,
    val timeseries: Boolean
)
package com.codegrow.remote.resources

import com.codegrow.entity.Rate

data class HistoricalResponse(
    val base: String,
    val end_date: String,
    val rates: HashMap<String, Rate>,
    val message: String,
    val start_date: String,
    val success: Boolean,
    val timeseries: Boolean
)
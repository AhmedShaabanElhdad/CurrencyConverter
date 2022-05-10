package com.codegrow.remote.resources

import com.codegrow.remote.resources.model.Rates

data class HistoricalResponse(
    val base: String,
    val end_date: String,
    val rates: HashMap<String,Rates>,
    val start_date: String,
    val success: Boolean,
    val timeseries: Boolean
)
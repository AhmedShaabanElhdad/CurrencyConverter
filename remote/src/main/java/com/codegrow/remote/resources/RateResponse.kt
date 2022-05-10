package com.codegrow.remote.resources

import com.codegrow.remote.resources.model.Rates

data class RateResponse(
    val base: String,
    val date: String,
    val historical: Boolean,
    val rates: HashMap<String,Rates>,
    val success: Boolean,
    val timestamp: Int
)
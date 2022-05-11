package com.codegrow.remote.resources

import com.codegrow.entity.Rate


data class RateResponse(
    val base: String,
    val date: String,
    val historical: Boolean,
    val rates: HashMap<String,Rate>,
    val success: Boolean,
    val timestamp: Int
)
package com.codegrow.remote.response

import com.codegrow.remote.response.model.Info
import com.codegrow.remote.response.model.Query

data class ConversionResponse(
    val date: String,
    val info: Info,
    val query: Query,
    val result: Double,
    val success: Boolean
)
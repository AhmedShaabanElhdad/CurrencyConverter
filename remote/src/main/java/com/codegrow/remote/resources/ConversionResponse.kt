package com.codegrow.remote.resources

import com.codegrow.remote.resources.model.Info
import com.codegrow.remote.resources.model.Query

data class ConversionResponse(
    val date: String,
    val info: Info,
    val query: Query,
    val result: Double,
    val success: Boolean
)
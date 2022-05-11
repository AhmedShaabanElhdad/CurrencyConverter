package com.codegrow.remote.resources

data class SymbolResponse(
    val success: Boolean,
    val message: String,
    val symbols: HashMap<String, String>
)
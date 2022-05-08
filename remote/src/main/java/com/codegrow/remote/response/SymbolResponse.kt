package com.codegrow.remote.response

data class SymbolResponse(
    val success: Boolean,
    val symbols: HashMap<String, String>
)
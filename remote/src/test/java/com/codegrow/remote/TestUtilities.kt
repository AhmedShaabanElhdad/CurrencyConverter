package com.codegrow.remote

import com.codegrow.remote.resources.ConversionResponse
import com.codegrow.remote.resources.HistoricalResponse
import com.codegrow.remote.resources.SymbolResponse
import com.codegrow.remote.service.convertFileName
import com.codegrow.remote.service.historicalRateFileName
import com.codegrow.remote.service.symbolFileName
import com.google.gson.Gson

import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */



class TestUtilities {

    companion object {
        fun getJson(path: String): String {
            val uri = this.javaClass.classLoader.getResource(path)
            val file = File(uri.path)
            return String(file.readBytes())
        }

        fun getSymbolsTestObject(): SymbolResponse {

            val gson = Gson()
            val testModel = gson.fromJson(getJson(symbolFileName), SymbolResponse::class.java)
            return  testModel

        }

        fun getConvertTestObject(): ConversionResponse {
            val gson = Gson()
            val testModel = gson.fromJson(getJson(convertFileName), ConversionResponse::class.java)
            return  testModel
        }

        fun getHistoricalTestObject(): HistoricalResponse {
            val gson = Gson()
            val testModel = gson.fromJson(getJson(historicalRateFileName), HistoricalResponse::class.java)
            return  testModel;
        }

    }
}
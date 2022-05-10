package com.codegrow.remote

import com.codegrow.remote.resources.SymbolResponse
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
            return  testModel;

        }

    }
}
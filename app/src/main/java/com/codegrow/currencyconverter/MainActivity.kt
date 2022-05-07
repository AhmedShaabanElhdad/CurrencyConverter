package com.codegrow.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codegrow.currencyconverter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    external fun getAPIKey(): String

    init {
        System.loadLibrary("currencyconverter")
    }
}
package com.example.sergey.currencyconverter.repository.api

import com.example.sergey.currencyconverter.repository.api.rates.RatesDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/latest")
    fun getLatestRates(@Query ("base") base: String? = "EUR"): Single<RatesDTO>
}
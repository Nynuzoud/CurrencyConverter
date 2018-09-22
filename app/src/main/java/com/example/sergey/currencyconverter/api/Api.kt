package com.example.sergey.currencyconverter.api

import com.example.sergey.currencyconverter.api.rates.RatesDTO
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/latest")
    fun getLatestRates(@Query ("base") base: String? = "EUR"): Observable<RatesDTO>
}
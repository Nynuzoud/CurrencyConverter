package com.example.sergey.currencyconverter.repository.rates

import com.example.sergey.currencyconverter.ui.rates.Rates
import com.example.sergey.currencyconverter.ui.vo.RatesVO
import io.reactivex.Completable
import io.reactivex.Observable

interface RatesRepository {

    fun getRatesRepository(base: Rates) : Completable
}
package com.example.sergey.currencyconverter.repository.interactor.rates

import com.example.sergey.currencyconverter.repository.data.Rates
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum
import io.reactivex.Observable

interface RatesRepository {

    fun getRatesRepository(base: CurrenciesEnum): Observable<Rates>
}
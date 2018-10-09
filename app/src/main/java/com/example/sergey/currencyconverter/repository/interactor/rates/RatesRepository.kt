package com.example.sergey.currencyconverter.repository.interactor.rates

import com.example.sergey.currencyconverter.repository.data.Rates
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum
import io.reactivex.Single

interface RatesRepository {

    fun getRatesRepository(base: CurrenciesEnum): Single<Rates>
}
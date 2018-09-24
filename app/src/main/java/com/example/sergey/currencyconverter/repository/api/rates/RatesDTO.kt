package com.example.sergey.currencyconverter.repository.api.rates

import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum

data class RatesDTO(

        var base: CurrenciesEnum,
        var date: String? = null,
        val ratesMap: MutableMap<CurrenciesEnum, Float>
)
package com.example.sergey.currencyconverter.api.rates

import com.example.sergey.currencyconverter.ui.rates.Rates
import java.util.*

data class RatesDTO(

        var base: String? = null,
        var date: String? = null,
        val ratesMap: MutableMap<Rates, Float> = EnumMap<Rates, Float>(Rates::class.java)
)
package com.example.sergey.currencyconverter.ui.vo

import com.example.sergey.currencyconverter.ui.rates.Rates

/**
 * Object needed to represent data to UI.
 * In this case we don't need this object so much,
 * but if we will want to add some UI feature which does not comes from server,
 * we can add them to this object without rewriting plenty of code.
 */
data class RatesVO(

        var date: String?,
        val ratesMap: MutableMap<Rates, Float>?
)
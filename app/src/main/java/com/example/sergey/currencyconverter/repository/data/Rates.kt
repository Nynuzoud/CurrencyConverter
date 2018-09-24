package com.example.sergey.currencyconverter.repository.data

import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum

/**
 * Object needed to represent data to UI.
 * In this case we don't need this object so much,
 * but if we will want to add some UI feature which does not comes from server,
 * we can add them to this object without rewriting plenty of code.
 */
data class Rates(

        var base: CurrenciesEnum,
        val ratesEnumMap: MutableMap<CurrenciesEnum, Float>
) {

    companion object {
        val DEFAULT_MULTIPLIER = 1f
    }
}
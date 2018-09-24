package com.example.sergey.currencyconverter.other.preferences

import android.content.Context
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum

class PreferencesImpl(private val context: Context) : Preferences {

    private val PREFS = "prefs"
    private val BASE_CARRENCY = "base_currency"

    private fun getPrefs() = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    override fun setBaseCurrency(currenciesEnum: CurrenciesEnum) {
        getPrefs().edit().putInt(BASE_CARRENCY, currenciesEnum.ordinal).apply()
    }

    override fun getBaseCurrency(): CurrenciesEnum {
        return CurrenciesEnum.values()[getPrefs().getInt(BASE_CARRENCY, CurrenciesEnum.EUR.ordinal)]
    }
}
package com.example.sergey.currencyconverter.ui.rates.adapter

import android.text.Editable
import android.text.TextWatcher
import com.example.sergey.currencyconverter.repository.data.Rates

/**
 * This class is needed to store and handle only one object of [BaseRateTextWatcher] to avoid different problems
 * The main problem is that any [TextWatcher] still working some time after calling [android.widget.TextView.removeTextChangedListener]
 * Solution is to add [isEnabled] property to check whether [TextWatcher] is enabled or not
 */
class BaseRateTextWatcher : TextWatcher {

    var ratesAdapterListener: RatesAdapterListener? = null

    var isEnabled = false

    private var oldValue: String = ""

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (!isEnabled) return

        val value = s?.toString() ?: Rates.DEFAULT_MULTIPLIER

        if (value == oldValue) return
        oldValue = value

        ratesAdapterListener?.onItemMultiplierEdit(value)
    }
}
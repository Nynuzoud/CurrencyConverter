package com.example.sergey.currencyconverter.ui.rates.adapter

import android.text.Editable
import android.text.TextWatcher
import com.example.sergey.currencyconverter.other.kotlinextensions.removeZeroAtStart

/**
 * This class is needed to store and handle only one object of [BaseRateTextWatcher] to avoid different problems
 * The main problem is that any [TextWatcher] still working some time after calling [android.widget.TextView.removeTextChangedListener]
 * Solution is to add [isEnabled] property to check whether [TextWatcher] is enabled or not
 */
class BaseRateTextWatcher : TextWatcher {

    var ratesAdapterListener: RatesAdapterListener? = null

    var isEnabled = false
    var oldValue = ""

    override fun afterTextChanged(s: Editable?) {
        if (!isEnabled) return

        val value = s?.toString()?.removeZeroAtStart() ?: return
        if (value != s.toString()) {
            s.replace(0, s.length, value)
            return
        }

        if (value == oldValue) return
        oldValue = value

        ratesAdapterListener?.onItemMultiplierEdit(value)

        ratesAdapterListener?.afterTextChanged()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}
package com.example.sergey.currencyconverter.other.kotlinextensions

import java.math.BigDecimal

fun CharSequence.toBigDecimalOrDefaultValue(defaultValue: BigDecimal? = null): BigDecimal {
    var result = defaultValue ?: BigDecimal("1")

    try {
        result = BigDecimal(this.toString())
    } catch (e: NumberFormatException) {

    }
    return result
}
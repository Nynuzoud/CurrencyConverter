package com.example.sergey.currencyconverter.other.kotlinextensions

import java.math.BigDecimal

fun CharSequence.toFloatOrDefaultValue(defaultValue: Float? = null): Float {
    var result = defaultValue ?: 0f

    try {
        result = toString().toFloat()
    } catch (e: NumberFormatException) {

    }
    return result
}

fun Float.round(scale: Int): Float {
    var bigDecimal = BigDecimal(this.toString())
    bigDecimal = bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_DOWN)
    return bigDecimal.toFloat()
}

fun Float.multiply(f1: Float, f2: Float) {

}
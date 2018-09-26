package com.example.sergey.currencyconverter.other.kotlinextensions

import java.lang.StringBuilder

fun CharSequence.removeZeroAtStart(): String {

    var result = ""

    if (this.isNotEmpty()) {
        result = if (this.first() == '0') {
            this.substring(1)
        } else this.toString()
    }

    if (result.isNotEmpty() && result.first() == '0') {
        result = result.removeZeroAtStart()
    }

    if (result.isEmpty()) result = "0"

    if (result.first() == '.') {
        val stringBuilder = StringBuilder("0")
        stringBuilder.append(result)
        result = stringBuilder.toString()
    }

    return result
}
package com.example.sergey.currencyconverter

import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class MultiplierTest {

    @Test
    fun multiply() {

        val first = BigDecimal("2.6598")
        val second = BigDecimal("4.4812")

        val expected = "11.91909576"

        val result = first.multiply(second).toString()
        assert(result == expected)

        val secondXResult = BigDecimal(result).multiply(second)
        val expected2 = "53.411851919712"

        assert(secondXResult.toString() == expected2)
    }

    @Test
    fun multiplyRounded() {
        val first = BigDecimal("2.6598")
        val second = BigDecimal("4.4812")

        val expected = "11.9190"

        val result = first.multiply(second).setScale(4, RoundingMode.DOWN).toString()
        assert(result == expected)

        val expectedFirstXExpected = "31.7021"

        val result2 = first.multiply(BigDecimal(expected)).setScale(4, RoundingMode.DOWN).toString()
        assert(result2 == expectedFirstXExpected)
    }
}
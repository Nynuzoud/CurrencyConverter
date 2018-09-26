package com.example.sergey.currencyconverter

import com.example.sergey.currencyconverter.other.kotlinextensions.removeZeroAtStart
import org.junit.Test

class RemoveZeroTest {

    @Test
    fun removeZeroTest() {

        val text1 = "0.00"
        val text2 = "0.01"
        val text3 = "1.00"
        val text4 = "01.00"
        val text5 = "01"
        val text6 = "010.00"
        val text7 = "010"
        val text8 = "0"
        val text9 = "000"
        val text10 = "0000000000"

        assert(text1.removeZeroAtStart() == "0.00")
        assert(text2.removeZeroAtStart() == "0.01")
        assert(text3.removeZeroAtStart() == "1.00")
        assert(text4.removeZeroAtStart() == "1.00")
        assert(text5.removeZeroAtStart() == "1")
        assert(text6.removeZeroAtStart() == "10.00")
        assert(text7.removeZeroAtStart() == "10")
        assert(text8.removeZeroAtStart() == "0")
        assert(text9.removeZeroAtStart() == "0")
        assert(text10.removeZeroAtStart() == "0")
    }
}
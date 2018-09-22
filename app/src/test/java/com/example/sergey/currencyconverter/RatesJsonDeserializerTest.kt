package com.example.sergey.currencyconverter

import com.example.sergey.currencyconverter.api.rates.RatesDTO
import com.example.sergey.currencyconverter.other.RatesJsonDeserializer
import com.example.sergey.currencyconverter.ui.rates.Rates
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class RatesJsonDeserializerTest {

    private val inputJson = "{\"base\":\"EUR\",\"date\":\"2018-09-06\",\"rates\":{\"AUD\":1.6191,\"BGN\":1.9591,\"BRL\":4.7998,\"CAD\":1.5364,\"CHF\":1.1294,\"CNY\":7.9584,\"CZK\":25.758,\"DKK\":7.4692,\"GBP\":0.89974,\"HKD\":9.1477,\"HRK\":7.4465,\"HUF\":327.04,\"IDR\":17352.0,\"ILS\":4.1776,\"INR\":83.857,\"ISK\":128.01,\"JPY\":129.77,\"KRW\":1306.9,\"MXN\":22.403,\"MYR\":4.82,\"NOK\":9.7923,\"NZD\":1.7662,\"PHP\":62.697,\"PLN\":4.3255,\"RON\":4.6463,\"RUB\":79.708,\"SEK\":10.608,\"SGD\":1.6027,\"THB\":38.194,\"TRY\":7.6409,\"USD\":1.1653,\"ZAR\":17.853}}"

    lateinit var gson: Gson

    @Before
    fun init() {
        gson = GsonBuilder()
                .registerTypeAdapter(RatesDTO::class.java, RatesJsonDeserializer())
                .create()
    }

    @Test
    fun checkDeserializationIsCorrect() {

        val ratesDTO = gson.fromJson(inputJson, RatesDTO::class.java)
        assertNotNull(ratesDTO)
        assert(ratesDTO.base == "EUR")
        assert(ratesDTO.date == "2018-09-06")
        assert(ratesDTO.ratesMap.containsKey(Rates.AUD))
        assert(ratesDTO.ratesMap.containsKey(Rates.ILS))
    }
}
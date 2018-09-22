package com.example.sergey.currencyconverter.other

import com.example.sergey.currencyconverter.api.rates.RatesDTO
import com.example.sergey.currencyconverter.ui.rates.Rates
import com.google.gson.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

/**
 * This class help to deserialize input JSON with a bunch of rates object
 * into an object containing [EnumMap] of rates.
 */
class RatesJsonDeserializer : JsonDeserializer<RatesDTO> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): RatesDTO? {

        val ratesDTO = RatesDTO()
        val base = json?.asJsonObject?.get("base")?.asString
        val date = json?.asJsonObject?.get("date")?.asString
        val rates = json?.asJsonObject?.get("rates") as JsonObject
        for (rate in rates.entrySet()) {
            var rateEnum: Rates? = null
            var rateValue: Float? =  null
            try {
                rateEnum = Rates.valueOf(rate.key)
                rateValue = rate.value.asFloat
            } catch (e: Exception) {
                Timber.d(e)
            }

            if (rateEnum != null && rateValue != null) ratesDTO.ratesMap[rateEnum] = rateValue
        }

        ratesDTO.base = base
        ratesDTO.date = date

        return ratesDTO
    }
}
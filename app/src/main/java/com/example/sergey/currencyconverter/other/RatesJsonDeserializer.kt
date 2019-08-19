package com.example.sergey.currencyconverter.other

import com.example.sergey.currencyconverter.repository.api.rates.RatesDTO
import com.example.sergey.currencyconverter.ui.rates.CurrenciesEnum
import com.google.gson.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

/**
 * This class helps to deserialize input JSON with a bunch of rates object
 * into an object containing [EnumMap] of rates.
 */
class RatesJsonDeserializer : JsonDeserializer<RatesDTO> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): RatesDTO? {

        val enumMap = EnumMap<CurrenciesEnum, String>(CurrenciesEnum::class.java)

        val base = json?.asJsonObject?.get("base")?.asString
        val date = json?.asJsonObject?.get("date")?.asString
        val rates = json?.asJsonObject?.get("rates") as JsonObject
        for (rate in rates.entrySet()) {
            var rateEnum: CurrenciesEnum? = null
            var rateValue: String? = null
            try {
                rateEnum = CurrenciesEnum.valueOf(rate.key)
                rateValue = rate.value.asString
            } catch (e: Exception) {
                Timber.d(e)
            }

            if (rateEnum != null && rateValue != null) enumMap[rateEnum] = rateValue
        }

        var ratesDTO: RatesDTO? = null

        CurrenciesEnum.values()
                .find { it.name == base }
                .let {
                    if (it == null) {
                        throw JsonParseException("Base currency is null")
                    } else {
                        ratesDTO = RatesDTO(it, date, enumMap)
                    }
                }

        return ratesDTO
    }
}
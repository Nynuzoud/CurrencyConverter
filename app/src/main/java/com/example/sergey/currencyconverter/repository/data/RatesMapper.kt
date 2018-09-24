package com.example.sergey.currencyconverter.repository.data

import com.example.sergey.currencyconverter.repository.api.rates.RatesDTO
import io.reactivex.functions.Function

class RatesMapper : Function<RatesDTO, Rates> {

    override fun apply(t: RatesDTO): Rates {
        return Rates(
                t.base,
                t.ratesMap
        )
    }
}
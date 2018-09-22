package com.example.sergey.currencyconverter.ui.vo

import com.example.sergey.currencyconverter.api.rates.RatesDTO
import io.reactivex.functions.Function

class RatesVOMapper : Function<RatesDTO, RatesVO> {

    override fun apply(t: RatesDTO): RatesVO {
        return RatesVO(
                t.date,
                t.ratesMap
        )
    }
}
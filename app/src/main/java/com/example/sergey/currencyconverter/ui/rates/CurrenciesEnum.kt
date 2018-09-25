package com.example.sergey.currencyconverter.ui.rates

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.sergey.currencyconverter.R

enum class CurrenciesEnum(@DrawableRes val imageRes: Int, @StringRes val currencyNameRes: Int) {

    AUD(R.drawable.ic_australia, R.string.currency_aud),
    BGN(R.drawable.ic_bulgaria, R.string.currency_bgn),
    BRL(R.drawable.ic_brazil, R.string.currency_brl),
    CAD(R.drawable.ic_canada, R.string.currency_cad),
    CHF(R.drawable.ic_switzerland, R.string.currency_chf),
    CNY(R.drawable.ic_china, R.string.currency_cny),
    CZK(R.drawable.ic_czech_republic, R.string.currency_czk),
    DKK(R.drawable.ic_denmark, R.string.currency_dkk),
    GBP(R.drawable.ic_united_kingdom, R.string.currency_gbp),
    HKD(R.drawable.ic_hong_kong, R.string.currency_hkd),
    HRK(R.drawable.ic_croatia, R.string.currency_hrk),
    HUF(R.drawable.ic_hungary, R.string.currency_huf),
    IDR(R.drawable.ic_indonesia, R.string.currency_idr),
    ILS(R.drawable.ic_israel, R.string.currency_ils),
    INR(R.drawable.ic_india, R.string.currency_inr),
    ISK(R.drawable.ic_iceland, R.string.currency_isk),
    JPY(R.drawable.ic_japan, R.string.currency_jpy),
    KRW(R.drawable.ic_south_korea, R.string.currency_krw),
    MXN(R.drawable.ic_mexico, R.string.currency_mxn),
    MYR(R.drawable.ic_malaysia, R.string.currency_myr),
    NOK(R.drawable.ic_norway, R.string.currency_nok),
    NZD(R.drawable.ic_new_zealand, R.string.currency_nzd),
    PHP(R.drawable.ic_philippines, R.string.currency_php),
    PLN(R.drawable.ic_poland, R.string.currency_pln),
    RON(R.drawable.ic_romania, R.string.currency_ron),
    RUB(R.drawable.ic_russia, R.string.currency_rub),
    SEK(R.drawable.ic_sweden, R.string.currency_sek),
    SGD(R.drawable.ic_singapore, R.string.currency_sgd),
    THB(R.drawable.ic_thailand, R.string.currency_thb),
    TRY(R.drawable.ic_turkey, R.string.currency_try),
    USD(R.drawable.ic_united_states_of_america, R.string.currency_usd),
    ZAR(R.drawable.ic_south_africa, R.string.currency_zar),
    EUR(R.drawable.ic_european_union, R.string.currency_eur)

}
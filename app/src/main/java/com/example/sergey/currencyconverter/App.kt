package com.example.sergey.currencyconverter

import android.app.Application
import com.example.sergey.currencyconverter.di.ComponentsHolder
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initDagger()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initDagger() {
        ComponentsHolder.init(this)
    }
}
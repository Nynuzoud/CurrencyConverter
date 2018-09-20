package com.example.sergey.currencyconverter.di

import android.app.Application
import com.example.sergey.currencyconverter.di.components.AppComponent
import com.example.sergey.currencyconverter.di.components.DaggerAppComponent
import com.example.sergey.currencyconverter.di.modules.AppModule

object ComponentsHolder {

    lateinit var applicationComponent: AppComponent

    fun init(application: Application) {

        applicationComponent = DaggerAppComponent.builder()
                .appModule(AppModule(application))
                .build()
    }
}
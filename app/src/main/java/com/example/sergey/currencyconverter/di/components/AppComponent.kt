package com.example.sergey.currencyconverter.di.components

import android.app.Application
import com.example.sergey.currencyconverter.api.Api
import com.example.sergey.currencyconverter.di.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    val application: Application
    val api: Api
}

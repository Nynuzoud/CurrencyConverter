package com.example.sergey.currencyconverter.di.components

import android.app.Application
import com.example.sergey.currencyconverter.di.modules.AppModule
import com.example.sergey.currencyconverter.repository.api.Api
import com.example.sergey.currencyconverter.ui.MainActivity
import com.example.sergey.currencyconverter.ui.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    val application: Application
    val api: Api

    fun inject(mainActivity: MainActivity)
    fun inject(mainViewModel: MainViewModel)
}

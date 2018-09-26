package com.example.sergey.currencyconverter.di.modules

import android.app.Application
import com.example.sergey.currencyconverter.BuildConfig
import com.example.sergey.currencyconverter.other.RatesJsonDeserializer
import com.example.sergey.currencyconverter.repository.api.Api
import com.example.sergey.currencyconverter.repository.api.rates.RatesDTO
import com.example.sergey.currencyconverter.repository.interactor.rates.RatesRepository
import com.example.sergey.currencyconverter.repository.interactor.rates.RatesRepositoryImpl
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    ///////////////////API//////////////////////

    @Singleton
    @Provides
    fun provideApiInterface(): Api {
        val logging = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.HEADERS
        }

        val client = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

        val gson = GsonBuilder()
                .registerTypeAdapter(RatesDTO::class.java, RatesJsonDeserializer())
                .create()

        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(Api::class.java)
    }

    ///////////////////REPOSITORIES//////////////////////

    @Provides
    @Singleton
    fun provideRatesRepository(api: Api): RatesRepository = RatesRepositoryImpl(api)
}
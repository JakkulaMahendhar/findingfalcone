package com.universe.findingfalcone.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.universe.findingfalcone.ui.utils.LocalProperties
import com.universe.findingfalcone.data.RepositoryImpl
import com.universe.findingfalcone.data.Service
import com.universe.findingfalcone.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val MY_PREF = "MyPref"
    private const val BASE_URL = "https://findfalcone.geektrust.com"

    private fun buildRetrofit(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

    @Provides
    @Singleton
    fun provideOkHttp(logger: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(logger)
            .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
//            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
//            } else {
//                HttpLoggingInterceptor.Level.NONE
//            }
        }

    @Provides
    @Singleton
    fun provideMyAPi(client: OkHttpClient): Service {
        return buildRetrofit(client).create(Service::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreference(application: Application): SharedPreferences =
        application.applicationContext.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideLocaleProperties(sharedPreferences: SharedPreferences) = LocalProperties(sharedPreferences)

    @Provides
    @Singleton
    fun provideMyRepository(service: Service, localProperties: LocalProperties): Repository {
        return RepositoryImpl(service,localProperties)
    }
}
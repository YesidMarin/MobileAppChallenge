package com.yesidmarin.mobileappchallenge.di

import com.yesidmarin.mobileappchallenge.BuildConfig
import com.yesidmarin.mobileappchallenge.network.MercadoLibreApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideMercadoLibreApi(retrofit: Retrofit): MercadoLibreApi {
        return retrofit.create(MercadoLibreApi::class.java)
    }
}
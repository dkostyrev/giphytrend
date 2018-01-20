package com.kostyrev.giphytrend.di

import com.google.gson.Gson
import com.kostyrev.giphytrend.BuildConfig
import com.kostyrev.giphytrend.api.ApiKeyInterceptor
import com.kostyrev.giphytrend.api.GiphyApi
import com.kostyrev.giphytrend.util.LoggingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideApi(client: OkHttpClient,
                   gson: Gson): GiphyApi {
        return Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .validateEagerly(BuildConfig.DEBUG)
                .baseUrl("https://api.giphy.com")
                .build()
                .create(GiphyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(ApiKeyInterceptor(BuildConfig.API_KEY))
                .addInterceptor(LoggingInterceptor())
                .build()
    }

}
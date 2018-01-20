package com.kostyrev.giphytrend.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.kostyrev.giphytrend.parse.UriTypeAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GsonModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(UriTypeAdapter())
                .create()
    }

    private inline fun <reified T> GsonBuilder.registerTypeAdapter(adapter: TypeAdapter<T>): GsonBuilder {
        return registerTypeAdapter(T::class.java, adapter)
    }

}
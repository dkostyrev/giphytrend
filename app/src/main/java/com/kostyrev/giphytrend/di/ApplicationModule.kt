package com.kostyrev.giphytrend.di

import com.kostyrev.giphytrend.GiphyTrendingApplication
import com.kostyrev.giphytrend.util.SchedulersFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: GiphyTrendingApplication) {

    @Provides
    @Singleton
    fun provideGiphyTrendingApplication(): GiphyTrendingApplication = application

    @Provides
    @Singleton
    fun provideSchedulers() = SchedulersFactory()

}
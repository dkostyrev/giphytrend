package com.kostyrev.giphytrend.di

import com.kostyrev.giphytrend.trending.di.TrendingComponent
import dagger.Component
import javax.inject.Singleton

@Component(
        modules = [
            ApplicationModule::class,
            GsonModule::class,
            ApiModule::class
        ])
@Singleton
interface ApplicationComponent {

    fun trendingComponent(): TrendingComponent

}
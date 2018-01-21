package com.kostyrev.giphytrend.di

import com.kostyrev.giphytrend.details.di.DetailsComponent
import com.kostyrev.giphytrend.details.di.DetailsModule
import com.kostyrev.giphytrend.trending.di.TrendingComponent
import com.kostyrev.giphytrend.trending.di.TrendingModule
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

    fun trendingComponent(module: TrendingModule): TrendingComponent

    fun detailsComponent(module: DetailsModule): DetailsComponent

}
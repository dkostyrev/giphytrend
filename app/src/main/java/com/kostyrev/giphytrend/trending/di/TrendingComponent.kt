package com.kostyrev.giphytrend.trending.di

import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.trending.TrendingActivity
import dagger.Subcomponent

@Subcomponent(modules = [TrendingModule::class])
@PerActivity
interface TrendingComponent {

    fun inject(activity: TrendingActivity)

}
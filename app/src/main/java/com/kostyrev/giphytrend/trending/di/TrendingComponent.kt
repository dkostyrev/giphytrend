package com.kostyrev.giphytrend.trending.di

import com.kostyrev.giphytrend.trending.TrendingActivity
import dagger.Subcomponent

@Subcomponent
interface TrendingComponent {

    fun inject(activity: TrendingActivity)

}
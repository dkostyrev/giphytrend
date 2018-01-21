package com.kostyrev.giphytrend.details.di

import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.details.DetailsActivity
import dagger.Subcomponent

@Subcomponent(modules = [DetailsModule::class])
@PerActivity
interface DetailsComponent {

    fun inject(activity: DetailsActivity)

}
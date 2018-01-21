package com.kostyrev.giphytrend.details.di

import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.details.DetailsState
import com.kostyrev.giphytrend.details.action.DetailsAction
import com.kostyrev.giphytrend.details.action.StartAction
import com.kostyrev.giphytrend.redux.ViewBinder
import com.kostyrev.giphytrend.util.SchedulersFactory
import com.kostyrev.redux.SubscribableStore
import dagger.Module
import dagger.Provides

@Module
class DetailsModule(private val id: String) {

    @PerActivity
    @Provides
    fun provideStore(): SubscribableStore<@JvmWildcard DetailsState, @JvmWildcard DetailsAction> {
        return SubscribableStore(emptyList(), emptyList(), DetailsState())
    }

    @PerActivity
    @Provides
    fun provideViewBinder(store: SubscribableStore<@JvmWildcard DetailsState, @JvmWildcard DetailsAction>,
                          schedulers: SchedulersFactory): ViewBinder<@JvmWildcard DetailsState, @JvmWildcard DetailsAction> {
        return ViewBinder(store, schedulers, startAction = StartAction())
    }


}
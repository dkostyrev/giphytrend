package com.kostyrev.giphytrend.trending.di

import com.kostyrev.giphytrend.StateHolder
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.redux.ViewBinder
import com.kostyrev.giphytrend.trending.TrendingState
import com.kostyrev.giphytrend.trending.action.StartAction
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.middleware.LoadTrendingMiddleware
import com.kostyrev.giphytrend.trending.middleware.NavigationMiddleware
import com.kostyrev.giphytrend.trending.reducer.LoadActionReducer
import com.kostyrev.giphytrend.util.SchedulersFactory
import com.kostyrev.redux.Store
import com.kostyrev.redux.SubscribableStore
import dagger.Module
import dagger.Provides

@Module
class TrendingModule(private val router: NavigationMiddleware.Router) {

    @PerActivity
    @Provides
    fun provideRouter() = router

    @PerActivity
    @Provides
    fun provideSubscribableStore(loadTrendingMiddleware: LoadTrendingMiddleware,
                                 navigationMiddleware: NavigationMiddleware,
                                 loadActionReducer: LoadActionReducer,
                                 stateHolder: StateHolder):
            SubscribableStore<@JvmWildcard TrendingState, @JvmWildcard TrendingAction> {
        return SubscribableStore(
                reducers = listOf(
                        loadActionReducer
                ),
                middleware = listOf(
                        loadTrendingMiddleware,
                        navigationMiddleware
                ),
                initialState = stateHolder.get() ?: TrendingState()
        )
    }

    @PerActivity
    @Provides
    fun provideStore(store: SubscribableStore<@JvmWildcard TrendingState, @JvmWildcard TrendingAction>):
            Store<@JvmWildcard TrendingState, @JvmWildcard TrendingAction> {
        return store
    }

    @PerActivity
    @Provides
    fun provideViewBinder(store: SubscribableStore<@JvmWildcard TrendingState, @JvmWildcard TrendingAction>,
                          schedulers: SchedulersFactory): ViewBinder<@JvmWildcard TrendingState, @JvmWildcard TrendingAction> {
        return ViewBinder(store, schedulers, startAction = StartAction())
    }

}
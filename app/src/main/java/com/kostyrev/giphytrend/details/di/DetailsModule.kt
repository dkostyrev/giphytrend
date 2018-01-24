package com.kostyrev.giphytrend.details.di

import com.kostyrev.giphytrend.api.GiphyApi
import com.kostyrev.giphytrend.details.DetailsInteractor
import com.kostyrev.giphytrend.details.DetailsState
import com.kostyrev.giphytrend.details.action.DetailsAction
import com.kostyrev.giphytrend.details.action.StartAction
import com.kostyrev.giphytrend.details.middleware.LoadDetailsMiddleware
import com.kostyrev.giphytrend.details.middleware.NavigationMiddleware
import com.kostyrev.giphytrend.details.reducer.LoadActionReducer
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.redux.ViewBinder
import com.kostyrev.giphytrend.util.LoggingMiddleware
import com.kostyrev.giphytrend.util.SchedulersFactory
import com.kostyrev.redux.Store
import com.kostyrev.redux.SubscribableStore
import dagger.Module
import dagger.Provides

@Module
class DetailsModule(private val id: String,
                    private val router: NavigationMiddleware.Router,
                    private val state: DetailsState?) {

    @PerActivity
    @Provides
    fun provideRouter() = router

    @PerActivity
    @Provides
    fun provideSubscribableStore(loadDetailsMiddleware: LoadDetailsMiddleware,
                                 navigationMiddleware: NavigationMiddleware,
                                 loadActionReducer: LoadActionReducer): SubscribableStore<@JvmWildcard DetailsState, @JvmWildcard DetailsAction> {
        return SubscribableStore(
                reducers = listOf(loadActionReducer),
                middleware = listOf(
                        LoggingMiddleware(),
                        loadDetailsMiddleware,
                        navigationMiddleware
                ),
                initialState = state ?: DetailsState()
        )
    }

    @PerActivity
    @Provides
    fun provideStore(store: SubscribableStore<@JvmWildcard DetailsState, @JvmWildcard DetailsAction>): Store<@JvmWildcard DetailsState, @JvmWildcard DetailsAction> {
        return store
    }

    @Provides
    @PerActivity
    fun provideInteractor(api: GiphyApi,
                          schedulers: SchedulersFactory): DetailsInteractor {
        return DetailsInteractor(id, api, schedulers)
    }

    @PerActivity
    @Provides
    fun provideViewBinder(store: SubscribableStore<@JvmWildcard DetailsState, @JvmWildcard DetailsAction>,
                          schedulers: SchedulersFactory): ViewBinder<@JvmWildcard DetailsState, @JvmWildcard DetailsAction> {
        return ViewBinder(store, schedulers, startAction = StartAction())
    }


}
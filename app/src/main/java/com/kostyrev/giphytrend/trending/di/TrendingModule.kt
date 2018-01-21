package com.kostyrev.giphytrend.trending.di

import android.util.Log
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.redux.ViewBinder
import com.kostyrev.giphytrend.trending.TrendingState
import com.kostyrev.giphytrend.trending.action.StartAction
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.middleware.LoadTrendingMiddleware
import com.kostyrev.giphytrend.trending.middleware.NavigationMiddleware
import com.kostyrev.giphytrend.trending.reducer.LoadActionReducer
import com.kostyrev.giphytrend.util.SchedulersFactory
import com.kostyrev.redux.Middleware
import com.kostyrev.redux.Store
import com.kostyrev.redux.SubscribableStore
import dagger.Module
import dagger.Provides
import io.reactivex.Observable

@Module
class TrendingModule(private val router: NavigationMiddleware.Router,
                     private val state: TrendingState? = null) {

    @PerActivity
    @Provides
    fun provideRouter() = router

    @PerActivity
    @Provides
    fun provideSubscribableStore(loadTrendingMiddleware: LoadTrendingMiddleware,
                                 navigationMiddleware: NavigationMiddleware,
                                 loadActionReducer: LoadActionReducer):
            SubscribableStore<@JvmWildcard TrendingState, @JvmWildcard TrendingAction> {
        return SubscribableStore(
                reducers = listOf(
                        loadActionReducer
                ),
                middleware = listOf(
                        LoggingMiddleware(),
                        loadTrendingMiddleware,
                        navigationMiddleware
                ),
                initialState = state ?: TrendingState()
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

    private class LoggingMiddleware : Middleware<TrendingState, TrendingAction> {
        override fun create(actions: Observable<TrendingAction>, state: Observable<TrendingState>): Observable<TrendingAction> {
            return actions.doOnNext {
                Log.i("Giphy", "${Thread.currentThread().name} Dispatching $it")
            }
        }
    }
}
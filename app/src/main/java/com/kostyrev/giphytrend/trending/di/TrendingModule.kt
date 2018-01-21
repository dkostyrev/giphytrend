package com.kostyrev.giphytrend.trending.di

import android.util.Log
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.trending.TrendingState
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.middleware.LoadTrendingMiddleware
import com.kostyrev.giphytrend.trending.reducer.LoadTrendingReducer
import com.kostyrev.giphytrend.util.SchedulersFactory
import com.kostyrev.redux.Middleware
import com.kostyrev.redux.Store
import com.kostyrev.redux.SubscribableStore
import dagger.Module
import dagger.Provides
import io.reactivex.Observable

@Module
class TrendingModule(private val state: TrendingState? = null) {

    @PerActivity
    @Provides
    fun provideSubscribableStore(loadTrendingMiddleware: LoadTrendingMiddleware,
                                 loadTrendingReducer: LoadTrendingReducer,
                                 schedulersFactory: SchedulersFactory):
            SubscribableStore<@JvmWildcard TrendingState, @JvmWildcard TrendingAction> {
        return SubscribableStore(
                reducers = listOf(
                        loadTrendingReducer
                ),
                middleware = listOf(
                        loadTrendingMiddleware,
                        LoggingMiddleware()
                ),
                scheduler = schedulersFactory.mainThread(),
                initialState = state ?: TrendingState()
        )
    }

    @PerActivity
    @Provides
    fun provideStore(store: SubscribableStore<@JvmWildcard TrendingState, @JvmWildcard TrendingAction>):
            Store<@JvmWildcard TrendingState, @JvmWildcard TrendingAction> {
        return store
    }


    private class LoggingMiddleware : Middleware<TrendingState, TrendingAction> {
        override fun create(actions: Observable<TrendingAction>, state: Observable<TrendingState>): Observable<TrendingAction> {
            return actions.doOnNext {
                Log.i("Giphy", "Dispatching $it")
            }
        }
    }
}
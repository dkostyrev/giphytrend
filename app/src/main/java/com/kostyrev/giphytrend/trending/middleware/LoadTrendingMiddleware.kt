package com.kostyrev.giphytrend.trending.middleware

import com.kostyrev.giphytrend.api.model.Pagination
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.trending.TrendingInteractor
import com.kostyrev.giphytrend.trending.TrendingState
import com.kostyrev.giphytrend.trending.TrendingState.LoadState
import com.kostyrev.giphytrend.trending.action.LoadAction
import com.kostyrev.giphytrend.trending.action.StartAction
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.action.TrendingViewAction.*
import com.kostyrev.redux.Middleware
import io.reactivex.Observable
import io.reactivex.rxkotlin.cast
import io.reactivex.rxkotlin.withLatestFrom
import javax.inject.Inject

@PerActivity
class LoadTrendingMiddleware @Inject constructor(private val interactor: TrendingInteractor) : Middleware<TrendingState, TrendingAction> {

    override fun create(actions: Observable<TrendingAction>, state: Observable<TrendingState>): Observable<TrendingAction> {
        return actions
                .withLatestFrom(state) { action, state -> action to state }
                .flatMap {
                    val (action, state) = it
                    when {
                        isLoading(action, state) -> {
                            loadData(pagination = null, startWith = LoadAction.Loading())
                        }
                        isRefreshing(action, state) -> {
                            loadData(pagination = null, startWith = LoadAction.Refreshing())
                        }
                        isAppending(action, state) -> {
                            loadData(state.pagination, startWith = LoadAction.Appending())
                        }
                        else -> Observable.empty()
                    }
                }
    }

    private fun isLoading(action: TrendingAction, state: TrendingState): Boolean {
        return when (action) {
            is StartAction -> state.isEmpty()
            is Retry -> state.loadState is LoadState.Loading
            else -> false
        }
    }

    private fun isAppending(action: TrendingAction, state: TrendingState): Boolean {
        return when (action) {
            is Append -> state.isIdle()
            is Retry -> state.loadState is LoadState.Appending
            else -> false
        }
    }

    private fun isRefreshing(action: TrendingAction, state: TrendingState): Boolean {
        return when (action) {
            is PullToRefresh -> state.isIdle()
            is Retry -> state.loadState is LoadState.Refreshing
            else -> false
        }
    }

    private fun TrendingState.isIdle(): Boolean {
        return loadState == null
    }

    private fun loadData(pagination: Pagination? = null, startWith: LoadAction): Observable<TrendingAction> {
        return interactor.loadTrending(pagination)
                .map { LoadAction.Loaded(it) }
                .cast<TrendingAction>()
                .onErrorReturn {
                    LoadAction.Error(it.message.orEmpty())
                }
                .startWith(startWith)
    }

    private fun TrendingState.isEmpty() = gifs.isEmpty() && error.isNullOrEmpty()

}
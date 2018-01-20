package com.kostyrev.giphytrend.trending.middleware

import com.kostyrev.giphytrend.api.model.Pagination
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.trending.TrendingInteractor
import com.kostyrev.giphytrend.trending.TrendingState
import com.kostyrev.giphytrend.trending.action.LoadAction
import com.kostyrev.giphytrend.trending.action.StartAction
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.action.TrendingViewAction
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
                        action is TrendingViewAction.EndOfListReached && !state.appending -> loadData(state.pagination)
                        action is StartAction && state.isEmpty() || action is TrendingViewAction.PullToRefreshStarted && !state.refreshing -> loadData()
                        else -> Observable.empty()
                    }
                }
    }

    private fun loadData(pagination: Pagination? = null): Observable<TrendingAction> {
        return interactor.loadTrending(pagination)
                .map { LoadAction.Loaded(it) }
                .cast<TrendingAction>()
                .onErrorReturn {
                    LoadAction.Error(it.message.orEmpty())
                }
                .startWith(LoadAction.Loading())
    }

    private fun TrendingState.isEmpty() = gifs.isEmpty() && !error

}
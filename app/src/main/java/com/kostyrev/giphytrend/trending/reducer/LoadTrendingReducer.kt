package com.kostyrev.giphytrend.trending.reducer

import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.trending.TrendingState
import com.kostyrev.giphytrend.trending.action.LoadAction
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.action.TrendingViewAction
import com.kostyrev.giphytrend.trending.list.GifItem
import com.kostyrev.redux.Reducer
import javax.inject.Inject

@PerActivity
class LoadTrendingReducer @Inject constructor() : Reducer<TrendingState, TrendingAction> {

    override fun reduce(state: TrendingState, action: TrendingAction): TrendingState {
        return when (action) {
            is TrendingViewAction.PullToRefreshStarted -> state.copy(refreshing = true)
            is TrendingViewAction.EndOfListReached -> state.copy(appending = true)
            is LoadAction.Loading -> {
                if (!state.refreshing && !state.appending) {
                    state.copy(loading = true)
                } else {
                    state
                }
            }
            is LoadAction.Error -> state.notLoading().copy(error = true)
            is LoadAction.Loaded -> with(action.result) {
                val state = if (!state.appending) {
                    state.copy(gifs = emptyList(), items = emptyList())
                } else {
                    state
                }
                state.notLoading().copy(
                        error = false,
                        gifs = state.gifs + data,
                        pagination = pagination,
                        items = state.items + data.toItems()
                )
            }
            else -> state
        }
    }

    private fun List<Gif>.toItems() = map {
        GifItem(it.id, it.images.fixedWidth.url)
    }

    private fun TrendingState.notLoading() = copy(
            loading = false,
            appending = false,
            refreshing = false
    )

}
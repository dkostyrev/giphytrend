package com.kostyrev.giphytrend.trending.reducer

import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.trending.TrendingState
import com.kostyrev.giphytrend.trending.action.LoadAction
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.list.GifItem
import com.kostyrev.redux.Reducer
import javax.inject.Inject

@PerActivity
class LoadActionReducer @Inject constructor() : Reducer<TrendingState, TrendingAction> {

    override fun reduce(state: TrendingState, action: TrendingAction): TrendingState {
        return when (action) {
            is LoadAction.Refreshing -> state.notLoading().copy(refreshing = true)
            is LoadAction.Appending -> state.notLoading().copy(appending = true)
            is LoadAction.Loading -> state.notLoading().copy(loading = true)
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
        GifItem(it.id, it.images.fixedWidth)
    }

    private fun TrendingState.notLoading() = copy(
            loading = false,
            appending = false,
            refreshing = false
    )

}
package com.kostyrev.giphytrend.trending.reducer

import android.util.Log
import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.Pagination
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.trending.TrendingState
import com.kostyrev.giphytrend.trending.TrendingState.LoadState.*
import com.kostyrev.giphytrend.trending.action.LoadAction
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.list.GifItem
import com.kostyrev.redux.Reducer
import javax.inject.Inject

@PerActivity
class LoadActionReducer @Inject constructor() : Reducer<TrendingState, TrendingAction> {

    override fun reduce(state: TrendingState, action: TrendingAction): TrendingState {
        return when (action) {
            is LoadAction.Refreshing -> state.copy(loadState = Refreshing(), error = null)
            is LoadAction.Appending -> state.copy(loadState = Appending(), error = null)
            is LoadAction.Loading -> state.copy(loadState = Loading(), error = null)
            is LoadAction.Error -> state.copy(error = action.error, canAppend = false)
            is LoadAction.Loaded -> with(action.result) {
                val state = if (state.loadState !is Appending) {
                    state.copy(gifs = emptyList(), items = emptyList())
                } else {
                    state
                }
                state.copy(
                        loadState = null,
                        error = null,
                        gifs = state.gifs + data,
                        pagination = pagination,
                        canAppend = pagination.canLoadMore(),
                        items = state.items + data.toItems()
                )
            }
            else -> state
        }
    }

    private fun Pagination.canLoadMore(): Boolean {
        return (count + offset) < totalCount
    }

    private fun List<Gif>.toItems() = map {
        GifItem(it.id, it.images.fixedWidth)
    }

}
package com.kostyrev.giphytrend.details.reducer

import com.kostyrev.giphytrend.details.DetailsState
import com.kostyrev.giphytrend.details.action.DetailsAction
import com.kostyrev.giphytrend.details.action.LoadAction
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.redux.Reducer
import javax.inject.Inject

@PerActivity
class LoadActionReducer @Inject constructor() : Reducer<DetailsState, DetailsAction> {

    override fun reduce(state: DetailsState, action: DetailsAction): DetailsState {
        return when (action) {
            is LoadAction.Loading -> state.copy(
                    loading = true,
                    error = null
            )
            is LoadAction.Loaded -> with(action.result) {
                state.copy(
                        loading = false,
                        error = null,
                        image = images.original,
                        user = user
                )
            }
            is LoadAction.Error -> state.copy(
                    loading = false,
                    error = action.error
            )
            else -> state
        }
    }
}
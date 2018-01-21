package com.kostyrev.giphytrend.trending.middleware

import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.trending.TrendingState
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.action.TrendingViewAction
import com.kostyrev.giphytrend.trending.list.GifItem
import com.kostyrev.redux.Middleware
import io.reactivex.Observable
import javax.inject.Inject

@PerActivity
class NavigationMiddleware @Inject constructor(private val router: Router) : Middleware<TrendingState, TrendingAction> {

    interface Router {

        fun openGifScreen(id: String)

    }

    override fun create(actions: Observable<TrendingAction>, state: Observable<TrendingState>): Observable<TrendingAction> {
        return actions
                .filter { it is TrendingViewAction.ListItemClicked }
                .doOnNext {
                    val action = it as TrendingViewAction.ListItemClicked
                    if (action.item is GifItem) {
                        router.openGifScreen(action.item.id)
                    }
                }
    }


}
package com.kostyrev.giphytrend.trending

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.kostyrev.giphytrend.R
import com.kostyrev.giphytrend.trending.action.TrendingViewAction
import com.kostyrev.giphytrend.trending.list.GifAdapter
import com.kostyrev.giphytrend.util.refreshes
import com.kostyrev.giphytrend.util.setVisible
import io.reactivex.Observable

class TrendingView(view: View) {

    private val swipeRefresh: SwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
    private val recycler: RecyclerView = view.findViewById(R.id.recycler_view)
    private val progress: View = view.findViewById(R.id.progress_bar)

    init {
        recycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    fun render(state: TrendingState) {
        swipeRefresh.setVisible(!state.loading)
        progress.setVisible(state.loading)
        swipeRefresh.isRefreshing = state.refreshing
        if (recycler.adapter == null) {
            recycler.adapter = GifAdapter(state.items)
        } else {
            (recycler.adapter as GifAdapter).data = state.items
        }
    }

    val actions: Observable<TrendingViewAction>
        get() = Observable.merge(listOf(
                swipeRefresh.refreshes.map { TrendingViewAction.PullToRefreshStarted() }

        ))

}


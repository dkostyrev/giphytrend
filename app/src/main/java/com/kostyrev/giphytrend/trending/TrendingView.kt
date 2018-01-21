package com.kostyrev.giphytrend.trending

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.jakewharton.rxrelay2.PublishRelay
import com.kostyrev.giphytrend.R
import com.kostyrev.giphytrend.list.AppendingAdapter
import com.kostyrev.giphytrend.list.ListItem
import com.kostyrev.giphytrend.trending.action.TrendingViewAction
import com.kostyrev.giphytrend.trending.list.GifAdapter
import com.kostyrev.giphytrend.util.refreshes
import com.kostyrev.giphytrend.util.setVisible
import io.reactivex.Observable

class TrendingView(view: View) {

    private val swipeRefresh: SwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
    private val recycler: RecyclerView = view.findViewById(R.id.recycler_view)
    private val progress: View = view.findViewById(R.id.progress_bar)
    private val clickRelay: PublishRelay<ListItem> = PublishRelay.create()
    private val adapter: GifAdapter = GifAdapter(emptyList(), clickRelay)

    init {
        val screenWidth = view.resources.displayMetrics.widthPixels
        val gifWidth = view.resources.getInteger(R.integer.gif_fixed_width)
        val spanCount = screenWidth / gifWidth
        recycler.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    }

    fun render(state: TrendingState) {
        swipeRefresh.setVisible(!state.loading)
        progress.setVisible(state.loading)
        swipeRefresh.isRefreshing = state.refreshing
        if (recycler.adapter == null) {
            recycler.adapter = adapter
        }
        adapter.canAppend = state.canAppend
        adapter.data = state.items
    }

    val actions: Observable<TrendingViewAction>
        get() = Observable.merge(listOf(
                swipeRefresh.refreshes.map { TrendingViewAction.PullToRefresh() },
                adapter.appends.map { TrendingViewAction.Append() },
                clickRelay.map { TrendingViewAction.ListItemClicked(it) }
        ))

    private val AppendingAdapter<*, *>.appends: Observable<Unit>
        get() {
            return Observable.create<Unit> {
                it.setCancellable { listener = null }
                listener = object : AppendingAdapter.AppendingListener {
                    override fun onAppend() {
                        it.onNext(Unit)
                    }

                }
            }
        }
}
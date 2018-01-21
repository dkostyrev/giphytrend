package com.kostyrev.giphytrend.trending

import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.jakewharton.rxrelay2.PublishRelay
import com.kostyrev.giphytrend.R
import com.kostyrev.giphytrend.list.AppendingAdapter
import com.kostyrev.giphytrend.list.ListItem
import com.kostyrev.giphytrend.redux.BoundableView
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.action.TrendingViewAction
import com.kostyrev.giphytrend.trending.list.GifAdapter
import com.kostyrev.giphytrend.util.refreshes
import com.kostyrev.giphytrend.util.setVisible
import io.reactivex.Observable

class TrendingView(private val view: View) : BoundableView<TrendingState, TrendingAction> {

    private val swipeRefresh: SwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
    private val recycler: RecyclerView = view.findViewById(R.id.recycler_view)
    private val progress: View = view.findViewById(R.id.progress_bar)
    private val clickRelay: PublishRelay<ListItem> = PublishRelay.create()
    private val retryActions: PublishRelay<Unit> = PublishRelay.create()
    private val adapter: GifAdapter = GifAdapter(emptyList(), clickRelay)
    private var snackbar: Snackbar? = null

    init {
        val screenWidth = view.resources.displayMetrics.widthPixels
        val gifWidth = view.resources.getDimensionPixelSize(R.dimen.gif_width)
        val spanCount = screenWidth / gifWidth
        recycler.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        adapter.setHasStableIds(true)
    }

    override fun render(state: TrendingState) {
        swipeRefresh.setVisible(!state.isLoading())
        progress.setVisible(state.isLoading())
        swipeRefresh.isRefreshing = state.isRefreshing()
        swipeRefresh.isEnabled = !state.hasError()
        if (recycler.adapter == null) {
            recycler.adapter = adapter
        }
        adapter.canAppend = state.canAppend
        adapter.data = state.items
        if (state.error != null) {
            if (snackbar == null) {
                snackbar = Snackbar.make(view, state.error, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry) {
                            snackbar = null
                            retryActions.accept(Unit)
                        }
                snackbar?.show()
            }
        } else {
            snackbar?.dismiss()
            snackbar = null
        }
    }

    override val actions: Observable<TrendingAction>
        get() = Observable.merge(listOf(
                swipeRefresh.refreshes.map { TrendingViewAction.PullToRefresh() },
                adapter.appends.map { TrendingViewAction.Append() },
                clickRelay.map { TrendingViewAction.ListItemClicked(it) },
                retryActions.map { TrendingViewAction.Retry() }
        ))

    private fun TrendingState.isLoading() = loadState is TrendingState.LoadState.Loading && !hasError()

    private fun TrendingState.isRefreshing() = loadState is TrendingState.LoadState.Refreshing && !hasError()

    private fun TrendingState.hasError() = !error.isNullOrEmpty()

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
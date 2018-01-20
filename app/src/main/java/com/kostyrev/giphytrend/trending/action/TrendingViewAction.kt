package com.kostyrev.giphytrend.trending.action

import com.kostyrev.giphytrend.list.ListItem

sealed class TrendingViewAction : TrendingAction {

    class PullToRefreshStarted : TrendingViewAction()

    class ListItemClicked(val item: ListItem) : TrendingViewAction()

    class EndOfListReached : TrendingViewAction()

}
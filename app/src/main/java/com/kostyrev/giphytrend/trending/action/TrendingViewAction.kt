package com.kostyrev.giphytrend.trending.action

import com.kostyrev.giphytrend.list.ListItem

sealed class TrendingViewAction : TrendingAction {

    class PullToRefresh : TrendingViewAction()

    class ListItemClicked(val item: ListItem) : TrendingViewAction()

    class Append : TrendingViewAction()

    class Retry : TrendingViewAction()

}
package com.kostyrev.giphytrend.trending

import com.kostyrev.giphytrend.trending.list.GifItem

data class TrendingState(val loading: Boolean,
                         val items: List<GifItem>)
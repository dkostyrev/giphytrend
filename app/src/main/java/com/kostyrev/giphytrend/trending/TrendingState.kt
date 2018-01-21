package com.kostyrev.giphytrend.trending

import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.Pagination
import com.kostyrev.giphytrend.trending.list.GifItem
import com.kostyrev.redux.State

data class TrendingState(val loading: Boolean = false,
                         val refreshing: Boolean = false,
                         val appending: Boolean = false,
                         val error: Boolean = false,
                         val canAppend: Boolean = true,
                         val items: List<GifItem> = emptyList(),
                         val gifs: List<Gif> = emptyList(),
                         val pagination: Pagination? = null) : State
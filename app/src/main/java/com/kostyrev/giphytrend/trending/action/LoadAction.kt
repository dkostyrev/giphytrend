package com.kostyrev.giphytrend.trending.action

import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.PagedResponse

sealed class LoadAction : TrendingAction {

    class Loading : LoadAction()

    class Appending : LoadAction()

    class Refreshing : LoadAction()

    class Loaded(val result: PagedResponse<List<Gif>>) : LoadAction()

    class Error(val error: String) : LoadAction()

}
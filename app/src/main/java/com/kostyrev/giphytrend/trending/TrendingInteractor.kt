package com.kostyrev.giphytrend.trending

import com.kostyrev.giphytrend.api.GiphyApi
import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.PagedResponse
import com.kostyrev.giphytrend.api.model.Pagination
import com.kostyrev.giphytrend.util.SchedulersFactory
import io.reactivex.Observable

class TrendingInteractor(private val api: GiphyApi,
                         private val schedulers: SchedulersFactory) {

    fun loadTrending(pagination: Pagination?): Observable<PagedResponse<List<Gif>>> {
        return api.getTrending(pagination?.offset)
                .subscribeOn(schedulers.io())

    }

}
package com.kostyrev.giphytrend.trending

import com.kostyrev.giphytrend.api.GiphyApi
import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.PagedResponse
import com.kostyrev.giphytrend.api.model.Pagination
import com.kostyrev.giphytrend.util.SchedulersFactory
import io.reactivex.Observable

interface TrendingInteractor {

    fun loadTrending(pagination: Pagination?): Observable<PagedResponse<List<Gif>>>

}

class TrendingInteractorImpl(private val api: GiphyApi,
                             private val schedulers: SchedulersFactory) : TrendingInteractor {

    override fun loadTrending(pagination: Pagination?): Observable<PagedResponse<List<Gif>>> {
        return api.getTrending(pagination?.offset)
                .subscribeOn(schedulers.io())
    }

}
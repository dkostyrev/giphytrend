package com.kostyrev.giphytrend.trending

import com.kostyrev.giphytrend.api.GiphyApi
import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.PagedResponse
import com.kostyrev.giphytrend.api.model.Pagination
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.util.SchedulersFactory
import io.reactivex.Observable
import javax.inject.Inject

@PerActivity
class TrendingInteractor @Inject constructor(private val api: GiphyApi,
                                             private val schedulers: SchedulersFactory) {

    fun loadTrending(pagination: Pagination?): Observable<PagedResponse<List<Gif>>> {
        return api.getTrending(pagination?.let { it.offset + it.count })
                .subscribeOn(schedulers.io())

    }

}
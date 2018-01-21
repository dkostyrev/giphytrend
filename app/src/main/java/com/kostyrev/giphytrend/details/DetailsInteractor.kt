package com.kostyrev.giphytrend.details

import com.kostyrev.giphytrend.api.GiphyApi
import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.Response
import com.kostyrev.giphytrend.util.SchedulersFactory
import io.reactivex.Observable

class DetailsInteractor constructor(private val id: String,
                                    private val api: GiphyApi,
                                    private val schedulers: SchedulersFactory) {

    fun loadGif(): Observable<Response<Gif>> {
        return api.getGif(id)
                .subscribeOn(schedulers.io())
    }

}
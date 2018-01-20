package com.kostyrev.giphytrend.api

import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.PagedResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {

    @GET("/v1/gifs/trending")
    fun getTrending(@Query("offset") offset: Int? = null): Observable<PagedResponse<List<Gif>>>

}
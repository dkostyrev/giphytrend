package com.kostyrev.giphytrend.api

import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.PagedResponse
import com.kostyrev.giphytrend.api.model.Response
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GiphyApi {

    @GET("/v1/gifs/trending")
    fun getTrending(@Query("offset") offset: Int? = null): Observable<PagedResponse<List<Gif>>>

    @GET("/v1/gifs/{id}")
    fun getGif(@Path("id") id: String): Observable<Response<Gif>>

}
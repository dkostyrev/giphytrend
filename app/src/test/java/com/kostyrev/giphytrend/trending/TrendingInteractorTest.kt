@file:Suppress("IllegalIdentifier")

package com.kostyrev.giphytrend.trending

import com.kostyrev.giphytrend.api.GiphyApi
import com.kostyrev.giphytrend.api.model.Pagination
import com.kostyrev.giphytrend.util.TestSchedulers
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Test

class TrendingInteractorTest {

    private val api: GiphyApi = mockApi()
    private val interactor = TrendingInteractor(api, TestSchedulers())

    @Test
    fun `load trending - loads trending without offset - no pagination provided`() {
        interactor.loadTrending(pagination = null).test()

        verify(api).getTrending(offset = null)
    }

    @Test
    fun `load trending - loads trending with offset - pagination provided`() {
        val pagination = Pagination(totalCount = 100, count = 20, offset = 20)

        interactor.loadTrending(pagination).test()

        verify(api).getTrending(offset = 40)
    }

    private fun mockApi(): GiphyApi = mock<GiphyApi>().also {
        whenever(it.getTrending(anyOrNull())).thenReturn(Observable.empty())
    }

}
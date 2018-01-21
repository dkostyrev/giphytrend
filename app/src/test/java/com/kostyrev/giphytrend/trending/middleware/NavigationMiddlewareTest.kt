@file:Suppress("IllegalIdentifier")

package com.kostyrev.giphytrend.trending.middleware

import com.jakewharton.rxrelay2.PublishRelay
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.action.TrendingViewAction
import com.kostyrev.giphytrend.trending.list.GifItem
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Test

class NavigationMiddlewareTest {

    private val router: NavigationMiddleware.Router = mock()
    private val actions = PublishRelay.create<TrendingAction>()

    @Test
    fun `list item clicked action - calls router with item id - item is gif item`() {
        val item = createGifItem(id = "id")
        create()

        actions.accept(TrendingViewAction.ListItemClicked(item))

        verify(router).openGifScreen("id")
    }

    private fun createGifItem(id: String) = GifItem(id, mock())

    private fun create(): TestObserver<TrendingAction> {
        return NavigationMiddleware(router).create(actions, Observable.empty()).test()
    }

}
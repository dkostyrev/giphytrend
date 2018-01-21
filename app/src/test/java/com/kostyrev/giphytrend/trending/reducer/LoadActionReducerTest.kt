@file:Suppress("IllegalIdentifier")

package com.kostyrev.giphytrend.trending.reducer

import com.kostyrev.giphytrend.api.model.*
import com.kostyrev.giphytrend.trending.TrendingState
import com.kostyrev.giphytrend.trending.action.LoadAction
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.list.GifItem
import com.kostyrev.giphytrend.util.Is
import com.nhaarman.mockito_kotlin.mock
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasSize
import org.hamcrest.core.Is
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class LoadActionReducerTest {

    private val reducer = LoadActionReducer()

    @Suppress("unused")
    private fun cases() = arrayOf(
            Case(
                    state = TrendingState(
                            refreshing = false,
                            appending = true,
                            loading = true
                    ),
                    action = LoadAction.Refreshing(),
                    expectedState = TrendingState(
                            refreshing = true,
                            appending = false,
                            loading = false
                    )
            ),
            Case(
                    state = TrendingState(
                            appending = false,
                            refreshing = true,
                            loading = true
                    ),
                    action = LoadAction.Appending(),
                    expectedState = TrendingState(
                            appending = true,
                            refreshing = false,
                            loading = false
                    )
            ),
            Case(
                    state = TrendingState(
                            loading = false,
                            appending = true,
                            refreshing = true
                    ),
                    action = LoadAction.Loading(),
                    expectedState = TrendingState(
                            loading = true,
                            appending = false,
                            refreshing = false
                    )
            ),
            Case(
                    state = TrendingState(
                            loading = true,
                            appending = true,
                            refreshing = true,
                            error = false
                    ),
                    action = LoadAction.Error(""),
                    expectedState = TrendingState(
                            error = true,
                            loading = false,
                            appending = false,
                            refreshing = false
                    )
            ),
            createResponse().let {
                Case(
                        state = TrendingState(
                                loading = true,
                                appending = true,
                                refreshing = true,
                                error = true
                        ),
                        action = LoadAction.Loaded(it),
                        expectedState = TrendingState(
                                loading = false,
                                appending = false,
                                refreshing = false,
                                error = false,
                                pagination = it.pagination
                        )
                )
            }
    )

    @Test
    @Parameters(method = "cases")
    fun `reduce - reduces to expected state according to action`(case: Case) {
        val reducedState = reducer.reduce(case.state, case.action)

        assertEquals(case.expectedState, reducedState)
    }

    @Test
    fun `reduce - converts gifs to items - loaded action`() {
        val image = createImage()
        val gif = createGif(id = "id", image = image)
        val action = LoadAction.Loaded(createResponse(listOf(gif)))
        val state = reducer.reduce(TrendingState(), action)

        with(state.items.first()) {
            assertThat(id, Is("id"))
            assertThat(image, Is(image))
        }
    }

    @Test
    fun `reduce - adds loaded gifs to state - state was appending`() {
        val originalGif = createGif()
        val loadedGif = createGif()
        val action = LoadAction.Loaded(createResponse(listOf(loadedGif)))
        val state = reducer.reduce(TrendingState(
                appending = true,
                gifs = listOf(originalGif)
        ), action)

        assertThat(state.gifs, Is(contains(originalGif, loadedGif)))
    }

    @Test
    fun `reduce - adds new items to state - state was appending`() {
        val action = LoadAction.Loaded(createResponse(listOf(createGif(id = "2"))))
        val state = reducer.reduce(TrendingState(
                appending = true,
                items = listOf(createItem(id = "1"))
        ), action)

        assertThat(state.items, hasSize(2))
        assertThat(state.items[1].id, Is("2"))
    }

    @Test
    fun `reduce - replaces items and gifs in state - state was not appending`() {
        val loadedGif = createGif()
        val action = LoadAction.Loaded(createResponse(listOf(loadedGif)))
        val state = reducer.reduce(TrendingState(
                appending = false,
                items = listOf(createItem(), createItem()),
                gifs = listOf(createGif(), createGif())
        ), action)

        assertThat(state.items, hasSize(1))
        assertThat(state.gifs, hasSize(1))
    }

    internal class Case(val state: TrendingState,
                        val action: TrendingAction,
                        val expectedState: TrendingState)

    private fun createResponse(gifs: List<Gif> = emptyList()): PagedResponse<List<Gif>> {
        return PagedResponse(gifs, Pagination(0, 0, 0))
    }

    private fun createGif(id: String = "id",
                          image: Image = createImage()): Gif {
        return Gif(
                id = id,
                title = "title",
                images = Images(fixedWidth = image, downsized = createImage()),
                user = null
        )
    }

    private fun createItem(id: String = "id"): GifItem {
        return GifItem(id = id, image = createImage())
    }

    private fun createImage(): Image {
        return Image(mock(), width = 0, height = 0, webp = mock())
    }
}
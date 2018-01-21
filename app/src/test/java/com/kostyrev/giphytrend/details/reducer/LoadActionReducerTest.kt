@file:Suppress("IllegalIdentifier")

package com.kostyrev.giphytrend.details.reducer

import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.Images
import com.kostyrev.giphytrend.details.DetailsState
import com.kostyrev.giphytrend.details.action.DetailsAction
import com.kostyrev.giphytrend.details.action.LoadAction
import com.kostyrev.giphytrend.util.Is
import com.nhaarman.mockito_kotlin.mock
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class LoadActionReducerTest {

    private val reducer = LoadActionReducer()

    @Suppress("unused")
    private fun cases() = arrayOf(
            Case(
                    state = DetailsState(
                            loading = false,
                            error = "error"
                    ),
                    action = LoadAction.Loading(),
                    expectedState = DetailsState(
                            loading = true,
                            error = null
                    )
            ),
            Case(
                    state = DetailsState(
                            loading = true,
                            error = null
                    ),
                    action = LoadAction.Error("error"),
                    expectedState = DetailsState(
                            loading = false,
                            error = "error"
                    )
            ),
            createGif().let {
                Case(
                        state = DetailsState(
                                loading = true,
                                image = null,
                                user = null
                        ),
                        action = LoadAction.Loaded(it),
                        expectedState = DetailsState(
                                loading = false,
                                image = it.images.original,
                                user = it.user
                        )
                )
            }
    )

    @Test
    @Parameters(method = "cases")
    fun `reduce - reduces to expected state according to action`(case: Case) {
        val reducedState = reducer.reduce(case.state, case.action)

        assertThat(case.expectedState, Is(reducedState))
    }

    private fun createGif(): Gif {
        return Gif(id = "id", title = "title", images = Images(fixedWidth = mock(), original = mock()), user = mock())
    }


    internal class Case(val state: DetailsState,
                        val action: DetailsAction,
                        val expectedState: DetailsState)

}
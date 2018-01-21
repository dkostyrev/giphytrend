@file:Suppress("IllegalIdentifier")

package com.kostyrev.giphytrend.trending.middleware

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.PagedResponse
import com.kostyrev.giphytrend.api.model.Pagination
import com.kostyrev.giphytrend.trending.TrendingInteractor
import com.kostyrev.giphytrend.trending.TrendingState
import com.kostyrev.giphytrend.trending.TrendingState.LoadState.*
import com.kostyrev.giphytrend.trending.action.LoadAction
import com.kostyrev.giphytrend.trending.action.StartAction
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.action.TrendingViewAction
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.hamcrest.Matchers.isA
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(JUnitParamsRunner::class)
internal class LoadTrendingMiddlewareTest {

    private val interactor: TrendingInteractor = mockInteractor()
    private val actions = PublishRelay.create<TrendingAction>()
    private val state = BehaviorRelay.createDefault(TrendingState())

    @Test
    fun `start action received - loads trending with interactor without pagination`() {
        create()

        actions.accept(StartAction())

        verify(interactor).loadTrending(pagination = null)
    }

    @Test
    fun `pull to refresh received - loads trending with interactor without pagination - load state is null`() {
        create()

        state.accept(TrendingState(loadState = null))
        actions.accept(TrendingViewAction.PullToRefresh())

        verify(interactor).loadTrending(pagination = null)
    }

    @Test
    fun `append received - loads trending with interactor with pagination - load state is null`() {
        val pagination: Pagination = mock()
        create()

        state.accept(TrendingState(loadState = null, pagination = pagination))
        actions.accept(TrendingViewAction.Append())

        verify(interactor).loadTrending(pagination)
    }

    @Test
    fun `start action received - does not load trending with interactor - state has loaded gifs`() {
        val observer = create()

        state.accept(TrendingState(gifs = listOf(mock())))
        actions.accept(StartAction())

        verify(interactor, never()).loadTrending(any())
        observer.assertNoValues()
    }

    @Suppress("unused")
    private fun positiveCases() = arrayOf(
            PositiveCase(StartAction(), TrendingState(loadState = null), LoadAction.Loading()),
            PositiveCase(TrendingViewAction.PullToRefresh(), TrendingState(loadState = null), LoadAction.Refreshing()),
            PositiveCase(TrendingViewAction.Append(), TrendingState(loadState = null), LoadAction.Appending())
    )

    @Test
    @Parameters(method = "positiveCases")
    fun `action received - emits expected action`(case: PositiveCase) {
        val observer = create()

        state.accept(case.state)
        actions.accept(case.action)

        val action = observer.values().first()
        assertThat(action, isA(case.expectedStart.javaClass))
    }

    @Test
    @Parameters(method = "positiveCases")
    fun `action received - emits loaded - data loaded`(case: PositiveCase) {
        val response: PagedResponse<List<Gif>> = mock()
        interactor.setResult(Observable.just(response))
        val observer = create()

        state.accept(case.state)
        actions.accept(case.action)

        observer.assertValueAt(1) {
            it is LoadAction.Loaded && it.result === response
        }
    }

    @Test
    @Parameters(method = "positiveCases")
    fun `action received - emits error - error received`(case: PositiveCase) {
        interactor.setResult(Observable.error(Throwable(message = "message")))
        val observer = create()

        state.accept(case.state)
        actions.accept(case.action)

        observer.assertValueAt(1) {
            it is LoadAction.Error && it.error == "message"
        }
    }

    @Suppress("unused")
    private fun negativeCases() = arrayOf(
            NegativeCase(StartAction(), TrendingState(gifs = listOf(mock()))),
            NegativeCase(TrendingViewAction.PullToRefresh(), TrendingState(loadState = Refreshing())),
            NegativeCase(TrendingViewAction.PullToRefresh(), TrendingState(loadState = Loading())),
            NegativeCase(TrendingViewAction.PullToRefresh(), TrendingState(loadState = Appending())),
            NegativeCase(TrendingViewAction.Append(), TrendingState(loadState = Refreshing())),
            NegativeCase(TrendingViewAction.Append(), TrendingState(loadState = Loading())),
            NegativeCase(TrendingViewAction.Append(), TrendingState(loadState = Appending()))
    )

    @Test
    @Parameters(method = "negativeCases")
    fun `action received - does not emit any action`(case: NegativeCase) {
        val observer = create()

        state.accept(case.state)
        actions.accept(case.action)

        observer.assertNoValues()
    }

    private fun TrendingInteractor.setResult(observable: Observable<PagedResponse<List<Gif>>> = Observable.empty()) {
        whenever(loadTrending(anyOrNull())).thenReturn(observable)
    }

    private fun mockInteractor() =
            mock<TrendingInteractor>().also {
                it.setResult(Observable.empty())
            }

    private fun create(): TestObserver<TrendingAction> {
        return LoadTrendingMiddleware(interactor).create(actions, state).test()
    }

    internal class PositiveCase(val action: TrendingAction,
                                val state: TrendingState,
                                val expectedStart: TrendingAction)

    internal class NegativeCase(val action: TrendingAction,
                                val state: TrendingState)

}
@file:Suppress("IllegalIdentifier")

package com.kostyrev.giphytrend.details.middleware

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.Response
import com.kostyrev.giphytrend.details.DetailsInteractor
import com.kostyrev.giphytrend.details.DetailsState
import com.kostyrev.giphytrend.details.action.DetailsAction
import com.kostyrev.giphytrend.details.action.DetailsViewAction
import com.kostyrev.giphytrend.details.action.LoadAction
import com.kostyrev.giphytrend.details.action.StartAction
import com.kostyrev.giphytrend.util.instanceOf
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.internal.matchers.InstanceOf

@RunWith(JUnitParamsRunner::class)
internal class LoadDetailsMiddlewareTest {

    private val interactor: DetailsInteractor = mockInteractor()
    private val actions = PublishRelay.create<DetailsAction>()
    private val state = BehaviorRelay.createDefault(DetailsState())


    @Suppress("unused")
    private fun positiveCases() = arrayOf(
            Case(StartAction(), DetailsState(loading = false)),
            Case(DetailsViewAction.Retry(), DetailsState(loading = false))
    )

    @Suppress("unused")
    private fun negativeCases() = arrayOf(
            Case(StartAction(), DetailsState(loading = true)),
            Case(StartAction(), DetailsState(image = mock(), user = mock())),
            Case(StartAction(), DetailsState(image = mock(), user = null)),
            Case(DetailsViewAction.Retry(), DetailsState(loading = true))
    )

    @Test
    @Parameters(method = "positiveCases")
    fun `action received - starts with loading action`(case: Case) {
        val observer = create()

        state.accept(case.state)
        actions.accept(case.action)

        observer.assertValueAt(0) { it is LoadAction.Loading }
    }

    @Test
    @Parameters(method = "positiveCases")
    fun `action received - emits loaded action with gif`(case: Case) {
        val gif = mock<Gif>()
        interactor.setResult(Observable.just(Response(gif)))
        val observer = create()

        state.accept(case.state)
        actions.accept(case.action)

        observer.assertValueAt(1) {
            it is LoadAction.Loaded && it.result === gif
        }
    }

    @Test
    @Parameters(method = "positiveCases")
    fun `action received - emits error action with gif`(case: Case) {
        interactor.setResult(Observable.error(Throwable("message")))
        val observer = create()

        state.accept(case.state)
        actions.accept(case.action)

        observer.assertValueAt(1) {
            it is LoadAction.Error && it.error == "message"
        }
    }

    @Test
    @Parameters(method = "negativeCases")
    fun `action received - does not start with loading action`(case: Case) {
        val observer = create()

        state.accept(case.state)
        actions.accept(case.action)

        observer.assertNoValues()
    }

    private fun create(): TestObserver<DetailsAction> {
        return LoadDetailsMiddleware(interactor).create(actions, state).test()
    }

    private fun DetailsInteractor.setResult(observable: Observable<Response<Gif>> = Observable.empty()) {
        whenever(loadGif()).thenReturn(observable)
    }

    private fun mockInteractor() = mock<DetailsInteractor>().also {
        it.setResult(Observable.empty())
    }

    internal class Case(val action: DetailsAction, val state: DetailsState)

}
package com.kostyrev.redux

import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.mockito.stubbing.Answer

internal class SubscribableStoreTest {

    @Test
    fun `create store - emits initial state`() {
        val initialState = TestState(value = 1)
        val store = createStore(state = initialState)

        val observer = store.stateChanges().test()

        observer.assertValueCount(1)
        observer.assertValue(initialState)
    }

    @Test
    fun `action dispatched - reduces state with reducers`() {
        val expectedState = TestState(value = 2)
        val store = createStore(
                state = TestState(value = 0),
                reducers = listOf(
                        reducer { state, _ ->
                            state.copy(value = 1)
                        },
                        reducer { state, _ ->
                            state.copy(value = state.value + 1)
                        }
                )
        )
        val observer = store.stateChanges().test()
        observer.values().clear()

        store.dispatch(TestAction())

        observer.assertValueCount(1)
        observer.assertValueAt(0) {
            it == expectedState
        }
    }

    @Test
    fun `action dispatched - propagates state to middleware`() {
        val state = TestState()
        val observer = TestObserver<TestState>()
        val store = createStore(state = state, middleware = listOf(middlewareWithStateObserver(observer)))
        observer.values().clear()

        store.dispatch(TestAction())

        observer.assertValueCount(1)
        observer.assertValueAt(0) {
            it == state
        }
    }

    @Test
    fun `action dispatched - propagates action to middleware`() {
        val action = TestAction()
        val observer = TestObserver<Action>()
        val store = createStore(middleware = listOf(middlewareWithActionObserver(observer)))
        observer.values().clear()

        store.dispatch(action)

        observer.assertValueCount(1)
        observer.assertValueAt(0) {
            it === action
        }
    }

    @Test
    fun `action dispatched - propagates action from middleware`() {
        val expectedState = TestState(value = 1)
        val store = createStore(
                reducers = listOf(
                        reducer { state, action ->
                            if (action is MiddlewareTestAction) {
                                state.copy(value = 1)
                            } else {
                                state
                            }
                        }
                ),
                middleware = listOf(
                        middleware { action, _ ->
                            action
                                    .filter { it is TestAction }
                                    .map { MiddlewareTestAction() }
                        }
                )
        )
        val observer = store.stateChanges().test()

        store.dispatch(TestAction())

        observer.assertValueCount(2)
        observer.assertValueAt(1) {
            it == expectedState
        }
    }

    private class MiddlewareTestAction : Action

    private class TestAction : Action

    private data class TestState(val value: Int = 0) : State

    private fun createStore(reducers: List<Reducer<TestState, Action>> = emptyList(),
                            middleware: List<Middleware<TestState, Action>> = emptyList(),
                            state: TestState = TestState()) =
            SubscribableStore(reducers, middleware, Schedulers.trampoline(), state).also { it.subscribe() }

    private fun reducer(reduceFunction: (TestState, Action) -> TestState) =
            object : Reducer<TestState, Action> {

                override fun reduce(state: TestState, action: Action): TestState {
                    return reduceFunction(state, action)
                }
            }

    private fun middlewareWithStateObserver(observer: TestObserver<TestState>) = middleware { actions, state ->
        state.subscribe(observer)
        actions
    }

    private fun middlewareWithActionObserver(observer: TestObserver<Action>) = middleware { actions, state ->
        actions.subscribe(observer)
        actions
    }

    private fun middleware(createFunction: (Observable<Action>, Observable<TestState>) -> Observable<Action>) =
            object : Middleware<TestState, Action> {

                override fun create(actions: Observable<Action>, state: Observable<TestState>): Observable<Action> {
                    return createFunction(actions, state)
                }
            }

}
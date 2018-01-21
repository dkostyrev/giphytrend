package com.kostyrev.redux

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.withLatestFrom

class SubscribableStore<S : State, in A : Action>(private val reducers: List<Reducer<S, A>>,
                                                  private val middleware: List<Middleware<S, A>>,
                                                  initialState: S) : Store<S, A> {

    private val state = BehaviorRelay.createDefault<S>(initialState)
    private val actions: Relay<A> = PublishRelay.create()

    fun subscribe(): Disposable {
        val disposable = CompositeDisposable()
        disposable += actions
                .withLatestFrom(state) { action, state -> action to state }
                .map {
                    val (action, latestState) = it
                    reducers.fold(latestState) { state, reducer -> reducer.reduce(state, action) }
                }
                .subscribe(state)

        disposable += Observable.merge(
                middleware.map {
                    it.create(actions, state.hide())
                            .withLatestFrom(actions) { middlewareAction, latestAction ->
                                middlewareAction to latestAction
                            }
                            .filter { it.first != it.second }
                            .map { it.first }
                })
                .subscribe(actions)
        return disposable
    }

    override fun dispatch(action: A) {
        actions.accept(action)
    }

    override fun stateChanges(): Observable<S> {
        return state
                .distinctUntilChanged()
                .hide()
    }

    override fun getState(): S {
        return state.value
    }

}
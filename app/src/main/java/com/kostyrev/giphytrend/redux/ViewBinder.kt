package com.kostyrev.giphytrend.redux

import com.kostyrev.giphytrend.util.SchedulersFactory
import com.kostyrev.redux.Action
import com.kostyrev.redux.State
import com.kostyrev.redux.SubscribableStore
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ViewBinder<out S : State, A : Action>(private val store: SubscribableStore<S, A>,
                                            private val schedulers: SchedulersFactory,
                                            private val startAction: A?) {

    private val disposable = CompositeDisposable()

    fun bind(view: BoundableView<S, A>) {
        disposable += store
                .stateChanges()
                .observeOn(schedulers.mainThread())
                .subscribe { view.render(it) }

        disposable += view.actions
                .subscribe { store.dispatch(it) }

        disposable += store.subscribe()

        startAction?.let { store.dispatch(it) }
    }

    fun unbind() {
        disposable.clear()
    }

}
package com.kostyrev.redux

import io.reactivex.Observable

interface Store<S : State, in A : Action> {

    fun dispatch(action: A)

    fun getState(): S

    fun stateChanges(): Observable<S>

}
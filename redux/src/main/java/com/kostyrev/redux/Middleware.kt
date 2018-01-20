package com.kostyrev.redux

import io.reactivex.Observable

interface Middleware<S : State, A : Action> {

    fun create(actions: Observable<A>, state: Observable<S>): Observable<A>

}
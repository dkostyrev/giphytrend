package com.kostyrev.redux

interface Reducer<S : State, in A : Action> {

    fun reduce(state: S, action: A): S

}
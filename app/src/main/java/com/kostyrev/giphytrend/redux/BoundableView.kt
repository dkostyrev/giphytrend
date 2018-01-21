package com.kostyrev.giphytrend.redux

import com.kostyrev.redux.Action
import com.kostyrev.redux.State
import io.reactivex.Observable

interface BoundableView<in S : State, A : Action> {

    fun render(state: S)

    val actions: Observable<A>

}
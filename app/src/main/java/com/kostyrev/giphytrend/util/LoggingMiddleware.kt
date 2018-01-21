package com.kostyrev.giphytrend.util

import android.util.Log
import com.kostyrev.redux.Action
import com.kostyrev.redux.Middleware
import com.kostyrev.redux.State
import io.reactivex.Observable

class LoggingMiddleware<S : State, A : Action> : Middleware<S, A> {
    override fun create(actions: Observable<A>, state: Observable<S>): Observable<A> {

        return actions.doOnNext {
            Log.i("Giphy", "${Thread.currentThread().name} Dispatching $it")
        }
    }
}
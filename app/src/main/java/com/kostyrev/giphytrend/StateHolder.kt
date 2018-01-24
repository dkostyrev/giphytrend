package com.kostyrev.giphytrend

import com.kostyrev.redux.State
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StateHolder @Inject constructor() {

    private val states = mutableMapOf<Class<out State>, State>()

    fun <T : State> set(state: T) {
        states[state::class.java] = state
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : State> get(clazz: Class<T>): T? {
        return states[clazz] as? T
    }

    inline fun <reified T : State> get(): T? {
        return get(T::class.java)
    }

}
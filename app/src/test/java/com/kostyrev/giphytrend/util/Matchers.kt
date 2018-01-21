package com.kostyrev.giphytrend.util

import org.hamcrest.Matchers

fun <T> Is(value: T) = Matchers.`is`(value)

inline fun <reified T : Any> instanceOf() = Matchers.instanceOf<T>(T::class.java)
package com.kostyrev.giphytrend.util

import org.hamcrest.Matchers

fun <T> Is(value: T) = Matchers.`is`(value)
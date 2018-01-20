package com.kostyrev.giphytrend.util

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class SchedulersFactory {

    fun io() = Schedulers.io()

    fun computation() = Schedulers.computation()

    fun mainThread() = AndroidSchedulers.mainThread()

}
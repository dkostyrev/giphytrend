package com.kostyrev.giphytrend.util

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class SchedulersFactory {

    open fun io() = Schedulers.io()

    open fun computation() = Schedulers.computation()

    open fun mainThread() = AndroidSchedulers.mainThread()

}
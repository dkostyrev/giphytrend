package com.kostyrev.giphytrend.util

import io.reactivex.schedulers.Schedulers

class TestSchedulers : SchedulersFactory() {

    override fun computation() = Schedulers.trampoline()

    override fun io() = Schedulers.trampoline()

    override fun mainThread() = Schedulers.trampoline()

}
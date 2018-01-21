package com.kostyrev.giphytrend.trending

import android.util.Log
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.giphytrend.trending.action.StartAction
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.util.SchedulersFactory
import com.kostyrev.redux.SubscribableStore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

@PerActivity
class TrendingViewBinder @Inject constructor(private val store: SubscribableStore<@JvmWildcard TrendingState, @JvmWildcard TrendingAction>,
                                             private val schedulers: SchedulersFactory) {

    private val disposable = CompositeDisposable()

    fun bind(view: TrendingView) {
        disposable += store
                .stateChanges()
                .observeOn(schedulers.mainThread())
                .doOnNext { Log.i("Giphy", "New state $it") }
                .subscribe { view.render(it) }

        disposable += view.actions
                .subscribe { store.dispatch(it) }

        disposable += store.subscribe()

        store.dispatch(StartAction())
    }

    fun unbind() {
        disposable.clear()
    }

}
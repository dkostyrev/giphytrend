package com.kostyrev.giphytrend.details.middleware

import com.kostyrev.giphytrend.details.DetailsInteractor
import com.kostyrev.giphytrend.details.DetailsState
import com.kostyrev.giphytrend.details.action.DetailsAction
import com.kostyrev.giphytrend.details.action.DetailsViewAction
import com.kostyrev.giphytrend.details.action.LoadAction
import com.kostyrev.giphytrend.details.action.StartAction
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.redux.Middleware
import io.reactivex.Observable
import io.reactivex.rxkotlin.cast
import io.reactivex.rxkotlin.withLatestFrom
import javax.inject.Inject

@PerActivity
class LoadDetailsMiddleware @Inject constructor(private val interactor: DetailsInteractor) : Middleware<DetailsState, DetailsAction> {

    override fun create(actions: Observable<DetailsAction>, state: Observable<DetailsState>): Observable<DetailsAction> {
        return actions
                .withLatestFrom(state) { action, state -> action to state }
                .filter {
                    val (action, state) = it
                    isStartAction(action, state) || isRetryAction(action, state)
                }.flatMap {
                    interactor.getGif()
                            .map { LoadAction.Loaded(it.data) }
                            .cast<DetailsAction>()
                            .startWith(LoadAction.Loading())
                            .onErrorReturn { LoadAction.Error(it.message.orEmpty()) }
                }
    }

    private fun isStartAction(action: DetailsAction, state: DetailsState): Boolean {
        return action is StartAction && state.isEmpty() && !state.loading
    }

    private fun isRetryAction(action: DetailsAction, state: DetailsState): Boolean {
        return action is DetailsViewAction.Retry && !state.loading
    }

    private fun DetailsState.isEmpty() = image == null && user == null

}
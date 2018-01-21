package com.kostyrev.giphytrend.details.middleware

import android.net.Uri
import com.kostyrev.giphytrend.details.DetailsState
import com.kostyrev.giphytrend.details.action.DetailsAction
import com.kostyrev.giphytrend.details.action.DetailsViewAction
import com.kostyrev.giphytrend.di.PerActivity
import com.kostyrev.redux.Middleware
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import javax.inject.Inject

@PerActivity
class NavigationMiddleware @Inject constructor(private val router: Router) : Middleware<DetailsState, DetailsAction> {

    interface Router {

        fun followUri(uri: Uri)

    }

    override fun create(actions: Observable<DetailsAction>, state: Observable<DetailsState>): Observable<DetailsAction> {
        return actions
                .withLatestFrom(state) { action, state -> action to state }
                .filter {
                    val (action, state) = it
                    action is DetailsViewAction.UserProfileClicked && state.user != null
                }
                .map { requireNotNull(it.second.user) }
                .doOnNext {
                    router.followUri(it.profile)
                }
                .flatMap { Observable.empty<DetailsAction>() }
    }

}
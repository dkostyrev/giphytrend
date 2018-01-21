package com.kostyrev.giphytrend.details

import android.view.View
import com.kostyrev.giphytrend.details.action.DetailsAction
import com.kostyrev.giphytrend.redux.BoundableView
import io.reactivex.Observable

class DetailsView(view: View) : BoundableView<DetailsState, DetailsAction> {

    override fun render(state: DetailsState) {

    }

    override val actions: Observable<DetailsAction>
        get() = Observable.empty()

}
package com.kostyrev.giphytrend.details

import android.view.View
import android.widget.TextView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.kostyrev.giphytrend.R
import com.kostyrev.giphytrend.details.action.DetailsAction
import com.kostyrev.giphytrend.details.action.DetailsViewAction
import com.kostyrev.giphytrend.redux.BoundableView
import com.kostyrev.giphytrend.util.bindText
import com.kostyrev.giphytrend.util.clicks
import com.kostyrev.giphytrend.util.setProgressBar
import com.kostyrev.giphytrend.util.setVisible
import io.reactivex.Observable
import io.reactivex.rxkotlin.cast

class DetailsView(view: View) : BoundableView<DetailsState, DetailsAction> {

    private val content: View = view.findViewById(R.id.content)
    private val progress: View = view.findViewById(R.id.progress_bar)
    private val draweeView: SimpleDraweeView = view.findViewById(R.id.drawee_view)
    private val avatar: SimpleDraweeView = view.findViewById(R.id.avatar)
    private val userInfo: View = view.findViewById(R.id.user_info)
    private val username: TextView = view.findViewById(R.id.username)
    private val name: TextView = view.findViewById(R.id.name)
    private val twitter: TextView = view.findViewById(R.id.twitter)
    private val error: TextView = view.findViewById(R.id.error_message)
    private val retry: View = view.findViewById(R.id.retry_button)

    init {
        draweeView.setProgressBar()
    }

    override fun render(state: DetailsState) {
        content.setVisible(!state.loading && !state.hasError())
        progress.setVisible(state.loading)
        error.bindText(state.error)
        retry.setVisible(state.hasError())
        state.image?.let {
            val aspectRatio = it.width.toFloat() / it.height.toFloat()
            val params = draweeView.layoutParams
            params.height = Math.round(draweeView.resources.displayMetrics.widthPixels / aspectRatio)
            draweeView.layoutParams = params
            draweeView.controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.controller)
                    .setAutoPlayAnimations(true)
                    .setUri(it.webp)
                    .build()
        }

        userInfo.setVisible(state.hasUser())
        twitter.setVisible(state.hasUser())
        state.user?.let {
            avatar.setImageURI(it.avatar)
            username.text = it.username
            name.text = it.displayName
            twitter.bindText(it.twitter)
        }
    }

    private fun DetailsState.hasUser() = user != null

    private fun DetailsState.hasError() = !error.isNullOrEmpty()

    override val actions: Observable<DetailsAction>
        get() = Observable.merge(listOf(
                userInfo.clicks.map { DetailsViewAction.UserProfileClicked() },
                retry.clicks.map { DetailsViewAction.Retry() }
        )).cast()

}
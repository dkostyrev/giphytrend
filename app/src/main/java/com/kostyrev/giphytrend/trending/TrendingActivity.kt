package com.kostyrev.giphytrend.trending

import android.os.Bundle
import android.view.View
import com.kostyrev.giphytrend.BaseActivity
import com.kostyrev.giphytrend.R
import com.kostyrev.giphytrend.di.ApplicationComponent
import com.kostyrev.giphytrend.details.DetailsActivity
import com.kostyrev.giphytrend.redux.ViewBinder
import com.kostyrev.giphytrend.trending.action.TrendingAction
import com.kostyrev.giphytrend.trending.di.TrendingModule
import com.kostyrev.giphytrend.trending.middleware.NavigationMiddleware
import com.kostyrev.redux.Store
import javax.inject.Inject

class TrendingActivity : BaseActivity(), NavigationMiddleware.Router {

    @Inject
    lateinit var store: Store<@JvmWildcard TrendingState, @JvmWildcard TrendingAction>
    @Inject
    lateinit var viewBinder: ViewBinder<@JvmWildcard TrendingState, @JvmWildcard TrendingAction>

    override fun injectSelf(applicationComponent: ApplicationComponent, savedInstanceState: Bundle?) {
        val state = savedInstanceState?.getParcelable<TrendingState>(KEY_STATE)
        applicationComponent
                .trendingComponent(TrendingModule(this, state))
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trending_activity)
        viewBinder.bind(TrendingView(findViewById<View>(android.R.id.content)))
    }

    override fun openGifScreen(id: String) {
        startActivity(DetailsActivity.Factory().createIntent(this, id))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_STATE, store.getState())
    }

    override fun onDestroy() {
        viewBinder.unbind()
        super.onDestroy()
    }

}

private const val KEY_STATE = "state"
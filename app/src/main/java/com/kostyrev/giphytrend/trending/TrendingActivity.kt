package com.kostyrev.giphytrend.trending

import android.os.Bundle
import android.view.View
import com.kostyrev.giphytrend.BaseActivity
import com.kostyrev.giphytrend.R
import com.kostyrev.giphytrend.StateHolder
import com.kostyrev.giphytrend.details.DetailsActivity
import com.kostyrev.giphytrend.di.ApplicationComponent
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
    lateinit var stateHolder: StateHolder
    @Inject
    lateinit var viewBinder: ViewBinder<@JvmWildcard TrendingState, @JvmWildcard TrendingAction>

    override fun injectSelf(applicationComponent: ApplicationComponent, savedInstanceState: Bundle?) {
        applicationComponent
                .trendingComponent(TrendingModule(this))
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

    override fun onDestroy() {
        viewBinder.unbind()
        stateHolder.set(store.getState())
        super.onDestroy()
    }

}

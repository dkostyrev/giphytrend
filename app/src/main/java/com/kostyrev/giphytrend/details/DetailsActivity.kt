package com.kostyrev.giphytrend.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kostyrev.giphytrend.BaseActivity
import com.kostyrev.giphytrend.R
import com.kostyrev.giphytrend.details.action.DetailsAction
import com.kostyrev.giphytrend.details.di.DetailsModule
import com.kostyrev.giphytrend.details.middleware.NavigationMiddleware
import com.kostyrev.giphytrend.di.ApplicationComponent
import com.kostyrev.giphytrend.redux.ViewBinder
import com.kostyrev.redux.Store
import javax.inject.Inject

class DetailsActivity : BaseActivity(), NavigationMiddleware.Router {

    @Inject
    lateinit var viewBinder: ViewBinder<@JvmWildcard DetailsState, @JvmWildcard DetailsAction>
    @Inject
    lateinit var store: Store<@JvmWildcard DetailsState, @JvmWildcard DetailsAction>

    override fun injectSelf(applicationComponent: ApplicationComponent, savedInstanceState: Bundle?) {
        val id = requireNotNull(intent?.getStringExtra(KEY_ID)) {
            "${DetailsActivity::class.java.simpleName} was created without $KEY_ID in Intent"
        }
        val state = savedInstanceState?.getParcelable<DetailsState>(KEY_STATE)
        applicationComponent
                .detailsComponent(DetailsModule(id, this, state))
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        viewBinder.bind(DetailsView(findViewById<View>(android.R.id.content)))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_STATE, store.getState())
    }

    override fun followUri(uri: Uri) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: Throwable) {
            Toast.makeText(this, "No activity can handle this action", Toast.LENGTH_SHORT).show()
        }
    }

    class Factory {

        fun createIntent(context: Context, id: String): Intent {
            return Intent(context, DetailsActivity::class.java)
                    .putExtra(KEY_ID, id)
        }

    }

}

private const val KEY_ID = "id"
private const val KEY_STATE = "state"
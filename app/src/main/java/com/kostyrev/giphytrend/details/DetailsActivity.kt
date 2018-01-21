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
import javax.inject.Inject

class DetailsActivity : BaseActivity(), NavigationMiddleware.Router {

    @Inject
    lateinit var viewBinder: ViewBinder<@JvmWildcard DetailsState, @JvmWildcard DetailsAction>

    override fun injectSelf(applicationComponent: ApplicationComponent, savedInstanceState: Bundle?) {
        val id = requireNotNull(intent?.getStringExtra(KEY_ID)) {
            "${DetailsActivity::class.java.simpleName} was created without $KEY_ID in Intent"
        }
        applicationComponent
                .detailsComponent(DetailsModule(id, this))
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        viewBinder.bind(DetailsView(findViewById<View>(android.R.id.content)))
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
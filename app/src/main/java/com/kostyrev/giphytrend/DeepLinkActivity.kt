package com.kostyrev.giphytrend

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.kostyrev.giphytrend.details.DetailsActivity

class DeepLinkActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Intent.ACTION_VIEW == intent.action) {
            val segments = intent.data?.pathSegments ?: return
            if (segments.isNotEmpty()) {
                val id = segments.last().split('-').lastOrNull() ?: return
                val intent = DetailsActivity.Factory().createIntent(this, id)
                startActivity(intent)
            }
        }
        finish()
    }

}
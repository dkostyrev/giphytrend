package com.kostyrev.giphytrend.trending

import android.os.Bundle
import com.kostyrev.giphytrend.BaseActivity
import com.kostyrev.giphytrend.di.ApplicationComponent

class TrendingActivity : BaseActivity() {

    override fun injectSelf(applicationComponent: ApplicationComponent, savedInstanceState: Bundle?) {
        applicationComponent
                .trendingComponent()
                .inject(this)
    }

}
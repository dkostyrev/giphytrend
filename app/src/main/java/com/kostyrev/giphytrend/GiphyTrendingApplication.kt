package com.kostyrev.giphytrend

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.kostyrev.giphytrend.di.ApplicationComponent
import com.kostyrev.giphytrend.di.ApplicationComponentProvider
import com.kostyrev.giphytrend.di.ApplicationModule
import com.kostyrev.giphytrend.di.DaggerApplicationComponent

class GiphyTrendingApplication : Application(), ApplicationComponentProvider {

    private lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        component = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    override val applicationComponent: ApplicationComponent
        get() {
            return component
        }

}
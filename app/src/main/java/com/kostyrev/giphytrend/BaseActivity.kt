package com.kostyrev.giphytrend

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kostyrev.giphytrend.di.ApplicationComponent
import com.kostyrev.giphytrend.di.ApplicationComponentProvider

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(requireNotNull(application as? ApplicationComponentProvider) {
            "$application must implement ${ApplicationComponentProvider::class.java.simpleName}"
        }) {
            injectSelf(applicationComponent, savedInstanceState)
        }
    }

    protected abstract fun injectSelf(applicationComponent: ApplicationComponent, savedInstanceState: Bundle?)

}
package com.kostyrev.giphytrend.deeplink

import android.content.Intent
import android.net.Uri
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.kostyrev.giphytrend.details.DetailsActivity
import com.kostyrev.giphytrend.details.KEY_ID
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeepLinkActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule<DeepLinkActivity>(DeepLinkActivity::class.java, true, false)

    @Before
    fun setUp() {
        Intents.init()
    }

    @Test
    fun `opens_details_activity__giphy_http_url_provided`() {
        val uri = Uri.parse("https://giphy.com/gifs/some-slug-3oFzmikX9Uh3995dhC")

        assertDetailsActivityStarted(uri, expectedId = "3oFzmikX9Uh3995dhC")
    }

    @Test
    fun `opens_details_activity__custom_scheme_provided`() {
        val uri = Uri.parse("com.kostyrev.giphytrending://gifs/3oFzmikX9Uh3995dhC")

        assertDetailsActivityStarted(uri, expectedId = "3oFzmikX9Uh3995dhC")
    }

    private fun assertDetailsActivityStarted(uri: Uri, expectedId: String) {
        val context = InstrumentationRegistry.getTargetContext()
        val intent = Intent(Intent.ACTION_VIEW, uri, context, DeepLinkActivity::class.java)
        activityRule.launchActivity(intent)

        Intents.intended(allOf(hasComponent(DetailsActivity::class.java.name), hasExtra(KEY_ID, expectedId)))
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}
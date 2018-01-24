package com.kostyrev.giphytrend.deeplink

import android.content.Intent
import android.net.Uri
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.hamcrest.core.Is
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeepLinkIntentFilterTest {

    private val context = InstrumentationRegistry.getTargetContext()

    @Test
    fun `giphy_https_url__resolves_to_details_activity`() {
        verifyUrl("https://giphy.com/gifs/some-slug-for-gif-d30rlxUkSTJWqE5W")
    }

    @Test
    fun `giphy_http_url__resolves_to_details_activity`() {
        verifyUrl("http://giphy.com/gifs/some-slug-for-gif-d30rlxUkSTJWqE5W")
    }

    @Test
    fun `giphy_www_https_url__resolves_to_details_activity`() {
        verifyUrl("https://www.giphy.com/gifs/some-slug-for-gif-d30rlxUkSTJWqE5W")
    }

    @Test
    fun `giphy_www_http_url__resolves_to_details_activity`() {
        verifyUrl("http://www.giphy.com/gifs/some-slug-for-gif-d30rlxUkSTJWqE5W")
    }

    @Test
    fun `custom_uri_scheme__resolves_to_details_activity`() {
        verifyUrl("com.kostyrev.giphytrending://gifs/some-slug-for-gif-d30rlxUkSTJWqE5W")
    }

    private fun verifyUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val resolveInfo = context.packageManager.queryIntentActivities(intent, 0)
        val info = resolveInfo.find { it.activityInfo.name == DeepLinkActivity::class.java.name && it.activityInfo.packageName == context.packageName }
        assertThat("Url $url haven't resolved to ${DeepLinkActivity::class.java.simpleName}", info, Is(not(nullValue())))
    }

}
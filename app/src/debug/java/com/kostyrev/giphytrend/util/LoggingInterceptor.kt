package com.kostyrev.giphytrend.util

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().also {
            Log.i(TAG, "-> ${it.method()} ${it.url()}")
        }
        return chain.proceed(request).also {
            Log.i(TAG, "<- ${request.method()} ${request.url()} ${it.code()}")
        }
    }
}

private const val TAG = "LoggingInterceptor"
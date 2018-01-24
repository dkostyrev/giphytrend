package com.kostyrev.giphytrend.util

import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}
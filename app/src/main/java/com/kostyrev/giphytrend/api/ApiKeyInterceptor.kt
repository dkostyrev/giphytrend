package com.kostyrev.giphytrend.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val key: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().newBuilder().addQueryParameter(QUERY_PARAMETER_KEY, key).build()
        return chain.proceed(request.newBuilder()
                .url(url)
                .build()
        )
    }
}

private const val QUERY_PARAMETER_KEY = "api_key"
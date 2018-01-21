package com.kostyrev.giphytrend.api.model

import com.google.gson.annotations.SerializedName

class Response<out T>(@SerializedName("data") val data: T)
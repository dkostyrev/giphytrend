package com.kostyrev.giphytrend.api.model

import com.google.gson.annotations.SerializedName

class PagedResponse<out T>(@SerializedName("data") val data: T?,
                           @SerializedName("pagination") val pagination: Pagination?)
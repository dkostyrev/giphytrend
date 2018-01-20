package com.kostyrev.giphytrend.api.model

import com.google.gson.annotations.SerializedName

class Pagination(@SerializedName("total_count") val totalCount: Int,
                 @SerializedName("count") val count: Int,
                 @SerializedName("offset") val offset: Int)
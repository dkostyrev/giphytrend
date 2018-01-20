package com.kostyrev.giphytrend.api.model

import com.google.gson.annotations.SerializedName

class Gif(@SerializedName("id") val id: String,
          @SerializedName("title") val title: String,
          @SerializedName("images") val images: Images,
          @SerializedName("user") val user: User?)
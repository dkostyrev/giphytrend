package com.kostyrev.giphytrend.api.model

import com.google.gson.annotations.SerializedName

class Images(@SerializedName("fixed_height") val fixedHeight: Image,
             @SerializedName("downsized_medium") val downsized: Image)
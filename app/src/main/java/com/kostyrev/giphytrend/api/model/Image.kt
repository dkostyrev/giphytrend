package com.kostyrev.giphytrend.api.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

class Image(@SerializedName("url") val url: Uri,
            @SerializedName("width") val width: Int,
            @SerializedName("height") val height: Int,
            @SerializedName("webp") val webp: Uri)
package com.kostyrev.giphytrend.api.model

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class Image(@SerializedName("url") val url: Uri,
            @SerializedName("width") val width: Int,
            @SerializedName("height") val height: Int,
            @SerializedName("webp") val webp: Uri?) : Parcelable
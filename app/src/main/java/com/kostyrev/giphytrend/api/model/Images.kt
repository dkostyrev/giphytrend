package com.kostyrev.giphytrend.api.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class Images(@SerializedName("fixed_width") val fixedWidth: Image,
             @SerializedName("downsized_medium") val downsized: Image) : Parcelable
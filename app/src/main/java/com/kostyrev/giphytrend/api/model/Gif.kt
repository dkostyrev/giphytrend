package com.kostyrev.giphytrend.api.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class Gif(@SerializedName("id") val id: String,
          @SerializedName("title") val title: String,
          @SerializedName("images") val images: Images,
          @SerializedName("user") val user: User?) : Parcelable
package com.kostyrev.giphytrend.api.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class Pagination(@SerializedName("total_count") val totalCount: Int,
                 @SerializedName("count") val count: Int,
                 @SerializedName("offset") val offset: Int) : Parcelable
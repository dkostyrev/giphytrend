package com.kostyrev.giphytrend.trending.list

import android.annotation.SuppressLint
import android.os.Parcelable
import com.kostyrev.giphytrend.api.model.Image
import com.kostyrev.giphytrend.list.ListItem
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class GifItem(override val id: String,
              val image: Image) : ListItem, Parcelable
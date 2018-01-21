package com.kostyrev.giphytrend.details

import android.annotation.SuppressLint
import android.os.Parcelable
import com.kostyrev.giphytrend.api.model.Image
import com.kostyrev.giphytrend.api.model.User
import com.kostyrev.redux.State
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class DetailsState(val loading: Boolean = false,
                        val error: String? = null,
                        val title: String? = null,
                        val image: Image? = null,
                        val user: User? = null) : State, Parcelable
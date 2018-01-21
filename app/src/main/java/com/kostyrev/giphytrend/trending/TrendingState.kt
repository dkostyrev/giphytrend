package com.kostyrev.giphytrend.trending

import android.annotation.SuppressLint
import android.os.Parcelable
import com.kostyrev.giphytrend.api.model.Gif
import com.kostyrev.giphytrend.api.model.Pagination
import com.kostyrev.giphytrend.trending.list.GifItem
import com.kostyrev.redux.State
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class TrendingState(val loadState: LoadState? = null,
                         val error: String? = null,
                         val canAppend: Boolean = false,
                         val items: List<GifItem> = emptyList(),
                         val gifs: List<Gif> = emptyList(),
                         val pagination: Pagination? = null) : State, Parcelable {

    sealed class LoadState : Parcelable {

        @Parcelize
        class Loading : LoadState()

        @Parcelize
        class Appending : LoadState()

        @Parcelize
        class Refreshing : LoadState()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }


    }

}
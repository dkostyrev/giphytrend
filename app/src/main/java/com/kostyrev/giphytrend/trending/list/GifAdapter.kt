package com.kostyrev.giphytrend.trending.list

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kostyrev.giphytrend.R
import com.kostyrev.giphytrend.list.AppendingAdapter

class GifAdapter(data: List<GifItem>) : AppendingAdapter<GifItemView, GifItem>(data) {

    override fun createAppendingViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return AppendingViewHolder(inflater.inflate(R.layout.appending_item, parent, false))
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GifItemView(inflater.inflate(R.layout.gif_item, parent, false))
    }

    private class AppendingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            val params = StaggeredGridLayoutManager.LayoutParams(view.layoutParams)
            params.isFullSpan = true
            view.layoutParams = params
        }

    }

}
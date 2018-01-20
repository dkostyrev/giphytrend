package com.kostyrev.giphytrend.trending.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kostyrev.giphytrend.R
import com.kostyrev.giphytrend.list.BaseAdapter

class GifAdapter(data: List<GifItem>) : BaseAdapter<GifItemView, GifItem>(data) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GifItemView(inflater.inflate(R.layout.gif_item, parent, false))
    }

}
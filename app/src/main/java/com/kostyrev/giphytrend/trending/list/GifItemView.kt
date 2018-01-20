package com.kostyrev.giphytrend.trending.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.facebook.drawee.view.SimpleDraweeView
import com.kostyrev.giphytrend.R
import com.kostyrev.giphytrend.list.ListItemView

class GifItemView(view: View) : ListItemView<GifItem>, RecyclerView.ViewHolder(view) {

    private val draweeView: SimpleDraweeView = view.findViewById(R.id.drawee_view)

    override fun render(item: GifItem) {
        draweeView.setImageURI(item.image, this)
    }

}
package com.kostyrev.giphytrend.trending.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RelativeLayout
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.kostyrev.giphytrend.R
import com.kostyrev.giphytrend.api.model.Image
import com.kostyrev.giphytrend.list.ListItem
import com.kostyrev.giphytrend.list.ListItemView
import com.kostyrev.giphytrend.util.setProgressBar
import io.reactivex.functions.Consumer

class GifItemView(view: View,
                  private val clickConsumer: Consumer<ListItem>) : ListItemView<GifItem>, RecyclerView.ViewHolder(view) {

    private val draweeView: SimpleDraweeView = view.findViewById(R.id.drawee_view)

    init {
        draweeView.setProgressBar()
    }

    override fun render(item: GifItem) = with(item) {
        itemView.setOnClickListener { clickConsumer.accept(item) }
        val previousImage = draweeView.getTag(R.id.gif_item_view_image) as? Image
        if (image.webp != previousImage?.webp) {
            with(draweeView.layoutParams as RelativeLayout.LayoutParams) {
                val aspectRatio = image.width.toFloat() / image.height.toFloat()
                val resources = draweeView.resources
                val padding = resources.getDimensionPixelSize(R.dimen.gif_item_padding)
                val width = resources.displayMetrics.widthPixels / resources.getInteger(R.integer.columns_count) - padding * 2
                height = Math.round(width / aspectRatio)
                draweeView.layoutParams = this
            }
            draweeView.controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.controller)
                    .setAutoPlayAnimations(true)
                    .setUri(image.webp)
                    .build()
            draweeView.setTag(R.id.gif_item_view_image, image)
        }
    }

}
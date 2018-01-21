package com.kostyrev.giphytrend.trending.list

import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RelativeLayout
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.kostyrev.giphytrend.R
import com.kostyrev.giphytrend.api.model.Image
import com.kostyrev.giphytrend.list.ListItem
import com.kostyrev.giphytrend.list.ListItemView
import com.kostyrev.giphytrend.util.CircleProgressBarDrawable
import io.reactivex.functions.Consumer

class GifItemView(view: View,
                  private val clickConsumer: Consumer<ListItem>) : ListItemView<GifItem>, RecyclerView.ViewHolder(view) {

    private val draweeView: SimpleDraweeView = view.findViewById(R.id.drawee_view)

    init {
        val resources = view.resources
        val progressDrawable = CircleProgressBarDrawable(resources)
        progressDrawable.color = ResourcesCompat.getColor(resources, R.color.progress, null)
        progressDrawable.backgroundColor = ResourcesCompat.getColor(resources, R.color.progress_background, null)
        draweeView.hierarchy.setProgressBarImage(progressDrawable)
    }

    override fun render(item: GifItem) = with(item) {
        itemView.setOnClickListener { clickConsumer.accept(item) }
        val previousImage = draweeView.getTag(R.id.gif_item_view_image) as? Image
        if (image.webp != previousImage?.webp) {
            with(draweeView.layoutParams as RelativeLayout.LayoutParams) {
                width = image.width
                height = image.height
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
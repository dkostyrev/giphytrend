package com.kostyrev.giphytrend.util

import android.support.v4.content.res.ResourcesCompat
import com.facebook.drawee.view.SimpleDraweeView
import com.kostyrev.giphytrend.R

fun SimpleDraweeView.setProgressBar() {
    val progressDrawable = CircleProgressBarDrawable(resources)
    progressDrawable.color = ResourcesCompat.getColor(resources, R.color.progress, null)
    progressDrawable.backgroundColor = ResourcesCompat.getColor(resources, R.color.progress_background, null)
    hierarchy.setProgressBarImage(progressDrawable)
}
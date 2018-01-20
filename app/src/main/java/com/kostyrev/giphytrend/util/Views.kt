package com.kostyrev.giphytrend.util

import android.view.View

val View.definedWidth: Int
    get() {
        if (width != 0) {
            return width
        } else if (measuredWidth != 0) {
            return measuredWidth
        } else {
            return Math.max(layoutParams?.width ?: 0, 0)
        }
    }

val View.definedHeight: Int
    get() {
        if (height != 0) {
            return height
        } else if (measuredHeight != 0) {
            return measuredHeight
        } else {
            return Math.max(layoutParams?.height ?: 0, 0)
        }
    }
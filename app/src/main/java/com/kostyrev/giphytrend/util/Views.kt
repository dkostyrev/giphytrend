package com.kostyrev.giphytrend.util

import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.TextView
import io.reactivex.Observable

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

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

fun TextView.bindText(text: String?) {
    this.text = text
    if (text.isNullOrEmpty()) {
        setVisible(false)
    } else {
        setVisible(true)
    }
}

val View.clicks: Observable<Unit>
    get() {
        return Observable.create { emitter ->
            emitter.setCancellable {
                setOnClickListener(null)
            }
            setOnClickListener {
                emitter.onNext(Unit)
            }
        }
    }

val SwipeRefreshLayout.refreshes: Observable<Unit>
    get() {
        return Observable.create {
            it.setCancellable {
                setOnRefreshListener(null)
            }
            setOnRefreshListener {
                it.onNext(Unit)
            }
        }
    }

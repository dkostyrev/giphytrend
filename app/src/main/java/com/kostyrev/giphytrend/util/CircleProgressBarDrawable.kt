package com.kostyrev.giphytrend.util

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.facebook.drawee.drawable.ProgressBarDrawable

/**
 * Original
 * https://github.com/facebook/fresco/blob/master/samples/contrib/com/facebook/drawee/drawable/CircleProgressBarDrawable.java
 */

class CircleProgressBarDrawable(resources: Resources) : ProgressBarDrawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var _level = 0

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = resources.displayMetrics.density * STROKE_WIDTH_DP
    }

    override fun onLevelChange(level: Int): Boolean {
        _level = level
        invalidateSelf()
        return true
    }

    override fun draw(canvas: Canvas) {
        if (hideWhenZero && _level == 0) {
            return
        }
        drawBar(canvas, MAX_LEVEL, backgroundColor)
        drawBar(canvas, _level, color)
    }

    private fun drawBar(canvas: Canvas, level: Int, color: Int) {
        paint.color = color
        val bounds = bounds
        val smallestSide = Math.min(bounds.width(), bounds.height())
        val size = smallestSide * .6f - smallestSide * .4f
        val left = bounds.left + bounds.width() / 2 - size / 2
        val bottom = bounds.bottom - bounds.height() / 2 + size / 2
        val top = bounds.top + bounds.height() / 2 - size / 2
        val right = bounds.right - bounds.width() / 2 + size / 2
        val rect = RectF(left, top, right, bottom)
        if (level != 0) {
            canvas.drawArc(rect, 0f, (level * 360 / MAX_LEVEL).toFloat(), false, paint)
        }
    }
}

private const val MAX_LEVEL = 10000
private const val STROKE_WIDTH_DP = 2
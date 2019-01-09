package com.app.o.custom

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import com.app.o.R


class RecyclerViewDecorator(private val context: Context) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        val drawableLine = ContextCompat.getDrawable(context, R.drawable.bg_separator)
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight
        val childCount = parent.childCount

        for (i in 0..childCount - 2) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + drawableLine!!.intrinsicHeight

            drawableLine.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            drawableLine.draw(canvas)
        }
    }

}
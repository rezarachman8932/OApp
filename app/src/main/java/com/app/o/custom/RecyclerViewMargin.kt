package com.app.o.custom

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class RecyclerViewMargin(private val space: Int) : RecyclerView.ItemDecoration() {

    companion object {
        const val DP_16 = 16
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildLayoutPosition(view)
        val adapter = parent.adapter

        if (adapter != null && position != adapter.itemCount - 1) {
            outRect.bottom = space
        }
    }

}
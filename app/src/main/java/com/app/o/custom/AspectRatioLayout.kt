package com.app.o.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.app.o.R

class AspectRatioLayout : RelativeLayout {

    private var mAspectRatioWidth: Int = 0
    private var mAspectRatioHeight: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioLayout)

        mAspectRatioWidth = a.getInt(R.styleable.AspectRatioLayout_aspectRatioWidth, 1)
        mAspectRatioHeight = a.getInt(R.styleable.AspectRatioLayout_aspectRatioHeight, 1)

        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val receivedWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val receivedHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        val aspectRatio = mAspectRatioWidth.toFloat() / mAspectRatioHeight

        val measuredWidth: Int
        val measuredHeight: Int
        val widthDynamic: Boolean

        widthDynamic = if (heightMode == View.MeasureSpec.EXACTLY) {
            if (widthMode == View.MeasureSpec.EXACTLY) {
                receivedWidth == 0
            } else {
                true
            }
        } else if (widthMode == View.MeasureSpec.EXACTLY) {
            false
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        if (widthDynamic) {
            val w = (receivedHeight * aspectRatio).toInt()
            measuredWidth = View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY)
            measuredHeight = heightMeasureSpec
        } else {
            measuredWidth = widthMeasureSpec
            val h = (receivedWidth / aspectRatio).toInt()
            measuredHeight = View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY)
        }

        super.onMeasure(measuredWidth, measuredHeight)
    }

}
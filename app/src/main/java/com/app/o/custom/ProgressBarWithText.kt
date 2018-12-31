package com.app.o.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.app.o.R
import kotlinx.android.synthetic.main.view_progress_bar.view.*

class ProgressBarWithText : LinearLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.view_progress_bar, this)

        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.ProgressBarWithText, defStyleAttr, 0)

        try {
            title = attributes.getString(R.styleable.ProgressBarWithText_progressTitle)
            subTitle = attributes.getString(R.styleable.ProgressBarWithText_progressSubTitle)
        } finally {
            attributes.recycle()
        }
    }

    var title : String?
        set(value) {
            field = value
            setProgressTitle()
        }

    var subTitle : String?
        set(value) {
            field = value
            setProgressSubTitle()
        }

    private fun setProgressTitle() {
        when {
            title != null -> {
                text_title_progress.text = title
            }
        }

        text_title_progress.invalidate()
        text_title_progress.requestLayout()
    }

    private fun setProgressSubTitle() {
        when {
            subTitle != null -> {
                text_subtitle_progress.text = subTitle
            }
        }

        text_subtitle_progress.invalidate()
        text_subtitle_progress.requestLayout()
    }

}
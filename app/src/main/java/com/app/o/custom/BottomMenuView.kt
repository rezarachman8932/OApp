package com.app.o.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.app.o.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.view_bottom_menu.view.*

class BottomMenuView: LinearLayout {

    private lateinit var clickListener : BottomMenuListener

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.view_bottom_menu, this)

        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.BottomMenuView, defStyleAttr, 0)

        try {
            isShowBadge = attributes.getBoolean(R.styleable.BottomMenuView_showBadge, false)
            imageURL = attributes.getString(R.styleable.BottomMenuView_imageUrl)
        } finally {
            attributes.recycle()
        }
    }

    var imageURL : String? = null
        set(value) {
            field = value
            displayImage()
        }

    var isShowBadge : Boolean = false
        set(value) {
            field = value
            showBadge()
        }

    fun setMenuListener(listener: (v: View, type: Int) -> Unit) {
        clickListener = object : BottomMenuListener {
            override fun onMenuClicked(v: View, actionType: Int) {
                listener(v, actionType)
            }
        }

        setViewListener()
    }

    private fun displayImage() {
        when {
            imageURL != null -> {
                view_image_third.load(imageURL!!)
            }

            imageURL == null -> {
                view_image_third.setImageResource(R.drawable.ic_vector_profile_bottom)
            }
        }

        view_image_third.invalidate()
        view_image_third.requestLayout()
    }

    private fun showBadge() {
        if (isShowBadge) {
            image_badge_message.visibility = View.VISIBLE
        } else {
            image_badge_message.visibility = View.GONE
        }

        image_badge_message.invalidate()
        image_badge_message.requestLayout()
    }

    private fun setViewListener() {
        layout_icon_first.setOnClickListener {
            clickListener.onMenuClicked(this, MESSAGE)
            layout_icon_first.invalidate()
            layout_icon_first.requestLayout()
        }

        layout_icon_second.setOnClickListener {
            clickListener.onMenuClicked(this, POST)
            layout_icon_second.invalidate()
            layout_icon_second.requestLayout()
        }

        layout_icon_third.setOnClickListener {
            clickListener.onMenuClicked(this, PROFILE)
            layout_icon_third.invalidate()
            layout_icon_third.requestLayout()
        }
    }

    private val picasso: Picasso
        get() = Picasso.get()

    private fun CircleImageView?.load(path: String) {
        picasso.load(path).into(this)
    }

    interface BottomMenuListener {
        fun onMenuClicked(v: View, actionType: Int)
    }

    companion object {
        const val MESSAGE = 1
        const val POST = 2
        const val PROFILE = 3
    }

}
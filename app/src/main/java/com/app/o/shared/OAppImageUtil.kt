package com.app.o.shared

import android.widget.ImageView
import com.squareup.picasso.Picasso

class OAppImageUtil {

    companion object {
        fun setImage(url: String?, holderSrc: Int?, imageView: ImageView) {
            if (!url.isNullOrEmpty() && holderSrc != null) {
                Picasso.get().load(url).placeholder(holderSrc).into(imageView)
            } else if (!url.isNullOrEmpty() && holderSrc == null) {
                Picasso.get().load(url).into(imageView)
            } else {
                Picasso.get().load(holderSrc!!).into(imageView)
            }
        }
    }

}
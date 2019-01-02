package com.app.o.post.multimedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.app.o.R
import com.app.o.base.page.OAppFragment

class PhotoVideoFragment : OAppFragment() {

    var index: Int? = -1

    companion object {
        private const val indexPage = "index"

        fun newInstance(index: Int): PhotoVideoFragment {
            val args = Bundle()
            args.putInt(indexPage, index)
            val fragment = PhotoVideoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        index = args?.getInt(indexPage)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_video, container, false)

        val imagePreview = view.findViewById(R.id.image_preview_post) as ImageView

        when (index) {
            0 -> imagePreview.setBackgroundResource(R.drawable.bg_default_post_image)
            1 -> imagePreview.setBackgroundResource(R.drawable.bg_default_post_video)
        }

        return view
    }

}
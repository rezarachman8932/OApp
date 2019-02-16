package com.app.o.detail.pager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.app.o.R
import com.app.o.shared.util.OAppMultimediaUtil

class DetailPostedImageFragment : Fragment() {

    private lateinit var imageUrl: String

    companion object {
        private const val indexUrl = "indexUrl"

        fun newInstance(url: String): DetailPostedImageFragment {
            val args = Bundle()
            args.putString(indexUrl, url)
            val fragment = DetailPostedImageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        imageUrl = args?.getString(indexUrl)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pager_thumb_detail, container, false)

        val imageDetailThumb = view.findViewById(R.id.item_image_detail_thumbnail) as ImageView
        OAppMultimediaUtil.setImage(imageUrl, null, imageDetailThumb)

        return view
    }

}
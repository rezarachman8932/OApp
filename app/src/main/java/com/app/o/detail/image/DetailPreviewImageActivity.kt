package com.app.o.detail.image

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.app.o.R
import com.app.o.shared.util.OAppMultimediaUtil
import com.app.o.shared.util.OAppUtil
import kotlinx.android.synthetic.main.activity_image_preview.*

class DetailPreviewImageActivity : AppCompatActivity() {

    private lateinit var selectedImage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        supportActionBar?.hide()

        getParam()
        setData()
    }

    private fun getParam() {
        selectedImage = intent.getStringExtra(OAppUtil.IMAGE_DETAIL_PREVIEW)
    }

    private fun setData() {
        OAppMultimediaUtil.setImage(selectedImage, null, image_preview)
    }

}
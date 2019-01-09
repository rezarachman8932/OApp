package com.app.o.detail

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.app.o.R
import com.app.o.api.detail.DetailResponseZip
import com.app.o.api.detail.DetailSpec
import com.app.o.api.media.MediaItem
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.custom.RecyclerViewDecorator
import com.app.o.shared.ImageUtil
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : OAppActivity(), OAppViewService<DetailResponseZip> {

    private lateinit var presenter: DetailPresenter
    private lateinit var adapter: DetailCommentAdapter
    private lateinit var postId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.title = getString(R.string.text_label_detail_header)

        getParam()

        initViewContent()

        presenter = DetailPresenter(this, mCompositeDisposable)
        presenter.geDetailPageContent(DetailSpec(postId))
    }

    override fun showLoading() {}

    override fun hideLoading(statusCode: Int) {}

    override fun onDataResponse(data: DetailResponseZip) {
        invalidateData(data)
    }

    private fun getParam() {
        postId = intent.getStringExtra("postId")
    }

    private fun initViewContent() {
        list_comment.layoutManager = LinearLayoutManager(this)
        list_comment.addItemDecoration(RecyclerViewDecorator(this))
    }

    private fun invalidateData(data: DetailResponseZip) {
        val media = data.detailContent.media[0]
        val contentData = data.detailContent
        val comments = data.commentListOptional.data

        when {
            contentData.type == "text" -> {
                layout_view_multimedia_content.visibility = View.GONE
            }

            contentData.type == "image" -> {
                image_detail_thumb.visibility = View.VISIBLE
                video_detail_thumb.visibility = View.GONE
                setMedia(media)
            }

            contentData.type == "video" -> {
                image_detail_thumb.visibility = View.GONE
                video_detail_thumb.visibility = View.VISIBLE
                setMedia(media)
            }
        }

        ImageUtil.setImage(null, R.drawable.ic_logo, image_detail_photo)

        text_detail_name.text = contentData.type
        text_detail_location_name.text = contentData.created_at
        text_detail_notes_tag.text = contentData.subtitle
        text_detail_love_count.text = contentData.like_count.toString()
        text_detail_title.text = contentData.title
        text_detail_content.text = contentData.content

        if (comments.isNotEmpty()) {
            text_detail_comment_count.text = comments.size.toString()

            adapter = DetailCommentAdapter()
            adapter.setListener {}
            adapter.setData(comments)

            list_comment.adapter = adapter

            adapter.notifyDataSetChanged()
        } else {
            card_view_comments.visibility = View.GONE
        }
    }

    private fun setMedia(media: MediaItem) {
        media.url?.let {
            if (media.type.equals("image", true)) {
                ImageUtil.setImage(it, null, image_detail_thumb)
            } else {
                //TODO Handle video content
            }
        }
    }

}
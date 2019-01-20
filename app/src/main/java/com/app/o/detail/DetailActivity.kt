package com.app.o.detail

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.app.o.R
import com.app.o.api.detail.DetailResponse
import com.app.o.api.detail.DetailResponseZip
import com.app.o.api.detail.DetailSpec
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.custom.RecyclerViewDecorator
import com.app.o.message.MessageActivity
import com.app.o.shared.OAppImageUtil
import com.app.o.shared.OAppUtil
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

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
        layout_content_view.visibility = View.INVISIBLE
    }

    override fun hideLoading(statusCode: Int) {
        progress_bar.visibility = View.INVISIBLE
        layout_content_view.visibility = View.VISIBLE
    }

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
        val contentData = data.detailContent
        val comments = data.commentListOptional.data
        val userProfile = data.userProfile

        when {
            contentData.type == "text" -> {
                layout_view_multimedia_content.visibility = View.GONE
            }

            contentData.type == "image" -> {
                image_detail_thumb.visibility = View.VISIBLE
                video_detail_thumb.visibility = View.GONE
                setMedia(contentData)
            }

            contentData.type == "video" -> {
                image_detail_thumb.visibility = View.GONE
                video_detail_thumb.visibility = View.VISIBLE
                setMedia(contentData)
            }
        }

        OAppImageUtil.setImage(null, R.drawable.ic_logo, image_detail_photo)

        text_detail_name.text = userProfile.name
        text_detail_location_name.text = userProfile.location
        text_detail_notes_tag.text = contentData.subtitle
        text_detail_love_count.text = contentData.like_count.toString()
        text_detail_title.text = contentData.title
        text_detail_content.text = contentData.content
        text_detail_time_ago.text = OAppUtil.getTimeAgo(OAppUtil.generateStringToTimestamp(contentData.created_at))

        if (comments.isNotEmpty()) {
            text_detail_comment_count.text = comments.size.toString()

            adapter = DetailCommentAdapter()
            adapter.setData(comments)
            adapter.setListener {
                val bundle = Bundle()
                val intent = Intent(this, MessageActivity::class.java)
                bundle.putParcelable("selectedComment", it)
                intent.putExtra("bundle", bundle)
                startActivity(intent)
            }

            list_comment.adapter = adapter

            adapter.notifyDataSetChanged()
        } else {
            card_view_comments.visibility = View.GONE
        }
    }

    private fun setMedia(detailResponse: DetailResponse) {
        if (!detailResponse.media.isNullOrEmpty()) {
            detailResponse.media[0].url.let {
                if (detailResponse.media[0].type.equals("image", true)) {
                    OAppImageUtil.setImage(it, null, image_detail_thumb)
                } else {
                    //TODO Handle video content
                }
            }
        }
    }

}
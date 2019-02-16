package com.app.o.detail

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.app.o.R
import com.app.o.api.detail.DetailResponse
import com.app.o.api.detail.DetailResponseZip
import com.app.o.api.detail.DetailSpec
import com.app.o.api.user.blocked.UserBlockingResponse
import com.app.o.api.user.blocked.UserBlockingSpec
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.custom.RecyclerViewDecorator
import com.app.o.detail.pager.DetailPostedImageAdapter
import com.app.o.message.room.MessageActivity
import com.app.o.message.submit.NewCommentActivity
import com.app.o.shared.util.OAppMultimediaUtil
import com.app.o.shared.util.OAppUtil
import com.app.o.user.blocked.UnblockedAccountCallback
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : OAppActivity(), OAppViewService<DetailResponseZip>, UnblockedAccountCallback {

    private lateinit var presenter: DetailPresenter
    private lateinit var adapter: DetailCommentAdapter
    private lateinit var postId: String
    private lateinit var contentData: DetailResponse
    private lateinit var imagePagerAdapter: DetailPostedImageAdapter
    private var isLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        getParam()

        initViewContent()

        presenter = DetailPresenter(this, this, mCompositeDisposable)
    }

    override fun onResume() {
        super.onResume()

        presenter.geDetailPageContent(DetailSpec(postId))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isLoaded) {
            if (contentData.type == "image") {
                menuInflater.inflate(R.menu.detail_menu, menu)
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_block_account -> {
            presenter.blockUser(UserBlockingSpec(16))
            true
        } else -> {
            super.onOptionsItemSelected(item)
        }
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

    override fun onProgress() {
        //TODO Show progress
    }

    override fun onSucceed(response: UserBlockingResponse) {
        //TODO Hide progress
    }

    override fun onFailed() {
        //TODO Hide progress
        //TODO Show snackBar
    }

    private fun getParam() {
        postId = intent.getStringExtra("postId")
        //TODO Get user_id to block function
    }

    private fun initViewContent() {
        supportActionBar?.title = getString(R.string.text_label_detail_header)

        list_comment.layoutManager = LinearLayoutManager(this)
        list_comment.addItemDecoration(RecyclerViewDecorator(this))

        icon_detail_post_new_comment.setOnClickListener {
            contentData.let { response ->
                val intent = Intent(this, NewCommentActivity::class.java)
                intent.putExtra("posted_id", response.post_id)
                intent.putExtra("posted_title", response.title)
                intent.putExtra("posted_subtitle", response.content)
                startActivity(intent)
            }
        }
    }

    private fun invalidateData(data: DetailResponseZip) {
        contentData = data.detailContent

        val comments = data.commentListOptional.data
        val userProfile = data.userProfile

        when {
            contentData.type == "text" -> {
                layout_view_multimedia_content.visibility = View.GONE
            }

            contentData.type == "image" -> {
                layout_group_image_slider.visibility = View.VISIBLE
                video_detail_thumb.visibility = View.GONE
                setMedia(contentData)
            }

            contentData.type == "video" -> {
                layout_group_image_slider.visibility = View.GONE
                video_detail_thumb.visibility = View.VISIBLE
                setMedia(contentData)
            }
        }

        OAppMultimediaUtil.setImage(null, R.drawable.ic_logo, image_detail_photo)

        text_detail_name.text = userProfile.name
        text_detail_location_name.text = userProfile.location
        text_detail_notes_tag.text = contentData.subtitle
        text_detail_love_count.text = contentData.like_count.toString()
        text_detail_title.text = contentData.title
        text_detail_content.text = contentData.content
        text_detail_time_ago.text = OAppUtil.getTimeAgo(OAppUtil.generateStringToTimestamp(contentData.created_at))

        if (comments.isNotEmpty()) {
            text_detail_comment_count.text = comments.size.toString()

            adapter = DetailCommentAdapter(this)
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

        isLoaded = true

        invalidateOptionsMenu()
    }

    private fun setMedia(detailResponse: DetailResponse) {
        if (!detailResponse.media.isNullOrEmpty()) {
            imagePagerAdapter = DetailPostedImageAdapter(supportFragmentManager, detailResponse.media)
            view_pager_posted_images.adapter = imagePagerAdapter
            pager_image_indicator.setupWithViewPager(view_pager_posted_images)
        }
    }

}
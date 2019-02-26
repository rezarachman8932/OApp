package com.app.o.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.MediaController
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
import com.app.o.shared.util.OAppUserUtil
import com.app.o.shared.util.OAppUtil
import com.app.o.user.blocked.UnblockedAccountCallback
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : OAppActivity(), OAppViewService<DetailResponseZip>, UnblockedAccountCallback, DetailInteractionCallback {

    private lateinit var presenter: DetailPresenter
    private lateinit var adapter: DetailCommentAdapter
    private lateinit var postId: String
    private lateinit var contentData: DetailResponse
    private lateinit var imagePagerAdapter: DetailPostedImageAdapter
    private lateinit var mediaController: MediaController

    private var isLoaded = false
    private var isInteractionLoading = false
    private var isLoveCountExist = false
    private var loveCount: Int = 0
    private var userId: Int = -1

    companion object {
        const val LIKE = 1
        const val DISLIKE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        getParam()

        initViewContent()

        presenter = DetailPresenter(this, this, this, mCompositeDisposable)
    }

    override fun onResume() {
        super.onResume()

        presenter.geDetailPageContent(DetailSpec(postId))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isLoaded) {
            if (userId != OAppUserUtil.getUserId()) {
                menuInflater.inflate(R.menu.detail_menu, menu)
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_block_account -> {
            presenter.blockUser(UserBlockingSpec(userId))
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

    override fun onIntegrationFailed() {
        isInteractionLoading = false
    }

    override fun onIntegrationSucceed(resultCode: Int) {
        isInteractionLoading = false

        if (resultCode == LIKE) {
            isLoveCountExist = true
            loveCount += 1
            icon_detail_love_count.setImageResource(R.drawable.ic_vector_heart_green)
        } else if (resultCode == DISLIKE) {
            isLoveCountExist = false
            loveCount -= 1
            icon_detail_love_count.setImageResource(R.drawable.ic_vector_heart_inactive)
        }

        text_detail_love_count.text = loveCount.toString()
    }

    private fun getParam() {
        postId = intent.getStringExtra(POST_ID)
        userId = intent.getIntExtra(USER_ID, -1)
    }

    private fun initViewContent() {
        supportActionBar?.title = getString(R.string.text_label_detail_header)

        list_comment.layoutManager = LinearLayoutManager(this)
        list_comment.addItemDecoration(RecyclerViewDecorator(this))

        mediaController = MediaController(this)
        mediaController.setAnchorView(video_detail_thumb)

        image_video_play.setOnClickListener {
            if (!video_detail_thumb.isPlaying) {
                image_video_play.visibility = View.GONE

                video_detail_thumb.visibility = View.VISIBLE
                video_detail_thumb.setMediaController(mediaController)
                video_detail_thumb.requestFocus()
                video_detail_thumb.start()
            }
        }

        icon_detail_post_new_comment.setOnClickListener {
            contentData.let { response ->
                val intent = Intent(this, NewCommentActivity::class.java)
                intent.putExtra(POSTED_ID, response.post_id)
                intent.putExtra(POSTED_TITLE, response.title)
                intent.putExtra(POSTED_SUBTITLE, response.content)
                startActivity(intent)
            }
        }

        icon_detail_love_count.setOnClickListener {
            if (isInteractionLoading) {
                return@setOnClickListener
            }

            isInteractionLoading = true

            if (isLoveCountExist) {
                presenter.doDislikeUserPost(postId, DISLIKE)
            } else {
                presenter.doLikeUserPost(postId, LIKE)
            }
        }
    }

    private fun invalidateData(data: DetailResponseZip) {
        contentData = data.detailContent

        val comments = data.commentListOptional.data

        when {
            contentData.type == OAppMultimediaUtil.TYPE_TEXT -> {
                layout_view_multimedia_content.visibility = View.GONE
            }

            contentData.type == OAppMultimediaUtil.TYPE_IMAGE -> {
                layout_group_image_slider.visibility = View.VISIBLE
                layout_group_video.visibility = View.GONE
                setMediaImage(contentData)
            }

            contentData.type == OAppMultimediaUtil.TYPE_VIDEO -> {
                layout_group_image_slider.visibility = View.GONE
                layout_group_video.visibility = View.VISIBLE
                setMediaVideo(contentData)
            }
        }

        isLoveCountExist = contentData.is_liked

        if (contentData.like_count > 0) {
            loveCount = contentData.like_count
        }

        if (isLoveCountExist) {
            icon_detail_love_count.setImageResource(R.drawable.ic_vector_heart_green)
        } else {
            icon_detail_love_count.setImageResource(R.drawable.ic_vector_heart_inactive)
        }

        OAppMultimediaUtil.setImage(contentData.avatar, R.drawable.ic_logo, image_detail_photo)

        text_detail_name.text = contentData.username
        text_detail_location_name.text = contentData.location.name
        text_detail_notes_tag.text = contentData.subtitle
        text_detail_love_count.text = loveCount.toString()
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
                bundle.putParcelable(SELECTED_COMMENT, it)
                intent.putExtra(BUNDLE, bundle)
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

    private fun setMediaImage(detailResponse: DetailResponse) {
        if (!detailResponse.media.isNullOrEmpty()) {
            imagePagerAdapter = DetailPostedImageAdapter(supportFragmentManager, detailResponse.media)
            view_pager_posted_images.adapter = imagePagerAdapter
            pager_image_indicator.setupWithViewPager(view_pager_posted_images)
        }
    }

    private fun setMediaVideo(detailResponse: DetailResponse) {
        if (!detailResponse.media.isNullOrEmpty()) {
            val uriPath = detailResponse.media[0].url
            val uri = Uri.parse(uriPath)
            video_detail_thumb.setVideoURI(uri)
        }
    }

}
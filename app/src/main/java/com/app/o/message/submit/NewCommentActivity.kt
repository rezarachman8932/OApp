package com.app.o.message.submit

import android.os.Bundle
import android.view.MenuItem
import com.app.o.R
import com.app.o.api.comment.CommentSpec
import com.app.o.api.comment.SubmitCommentResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import kotlinx.android.synthetic.main.activity_new_message.*

class NewCommentActivity : OAppActivity(), OAppViewService<SubmitCommentResponse> {

    private var postId: Int = 0
    private lateinit var postTitle: String
    private lateinit var postSubtitle: String
    private lateinit var presenter: NewCommentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        getParam()

        initView()

        presenter = NewCommentPresenter(this, mCompositeDisposable)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading(statusCode: Int) {

    }

    override fun onDataResponse(data: SubmitCommentResponse) {
        if (isSuccess(data.status)) {
            finish()
        }
    }

    private fun getParam() {
        postId = intent.getIntExtra("posted_id", 0)
        postTitle = intent.getStringExtra("posted_title")
        postSubtitle = intent.getStringExtra("posted_subtitle")
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.text_label_create_comment_header)

        text_post_comment_title.text = postTitle
        text_post_comment_subtitle.text = postSubtitle

        button_submit_new_comment.setOnClickListener {
            val comment = input_new_comment.text.toString()

            if (comment.isNotEmpty()) {
                presenter.submitNewComment(CommentSpec(postId.toString(), comment, null))
            }
        }
    }

}
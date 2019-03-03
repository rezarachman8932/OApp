package com.app.o.message.room

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.app.o.R
import com.app.o.api.comment.Comment
import com.app.o.api.comment.CommentSpec
import com.app.o.api.comment.SubmitCommentResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppSubmitMessageService
import com.app.o.custom.RecyclerViewMargin
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : OAppActivity(), OAppSubmitMessageService {

    private lateinit var presenter: MessagePresenter
    private lateinit var adapter: MessageAdapter
    private lateinit var parentComment: Comment
    private lateinit var comments: MutableList<Comment>
    private var isSubmittingComment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        getParam()

        initView()
        invalidateData()

        presenter = MessagePresenter(this, mCompositeDisposable)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMessageBeingProcessed() {
        isSubmittingComment = true
    }

    override fun onMessageNotSent() {
        isSubmittingComment = false
    }

    override fun onMessageSent(submitMessageResponse: SubmitCommentResponse) {
        isSubmittingComment = false

        val newRepliedComment = Comment(
                submitMessageResponse.comment_id,
                submitMessageResponse.post_id,
                submitMessageResponse.user_id,
                submitMessageResponse.username,
                submitMessageResponse.avatar,
                submitMessageResponse.content,
                submitMessageResponse.created_at,
                null, null, null)

        comments.add(newRepliedComment)

        adapter.setMessages(comments)
        adapter.notifyDataSetChanged()

        input_comment.setText("")
        recycler_view_message.smoothScrollToPosition(comments.size - 1)
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = parentComment.username

        adapter = MessageAdapter()
        recycler_view_message.adapter = adapter

        recycler_view_message.layoutManager = LinearLayoutManager(this)
        recycler_view_message.addItemDecoration(RecyclerViewMargin(RecyclerViewMargin.DP_16))

        button_send_comment.setOnClickListener {
            val inputString = input_comment.text.toString()

            if (!isSubmittingComment && !inputString.isEmpty()) {
                val spec = CommentSpec(
                        parentComment.post_id.toString(),
                        inputString,
                        getDeviceToken()!!,
                        parentComment.comment_id.toString())
                presenter.postReplyComment(spec)
            }
        }
    }

    private fun getParam() {
        val bundle = intent.getBundleExtra(BUNDLE)
        parentComment = bundle.getParcelable(SELECTED_COMMENT) as Comment
    }

    private fun invalidateData() {
        comments = parentComment.reply!!

        if (comments.size > 0) {
            adapter.setMessages(comments)
            adapter.notifyDataSetChanged()
            recycler_view_message.scrollToPosition(comments.size - 1)
        }
    }

}
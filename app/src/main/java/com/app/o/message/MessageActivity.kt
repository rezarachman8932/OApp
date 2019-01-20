package com.app.o.message

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import com.app.o.R
import com.app.o.api.comment.Comment
import com.app.o.api.comment.CommentResponse
import com.app.o.api.comment.CommentSpec
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppSubmitMessageService
import com.app.o.base.service.OAppViewService
import com.app.o.custom.RecyclerViewMargin
import com.app.o.shared.OAppUtil
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : OAppActivity(), OAppViewService<CommentResponse>, OAppSubmitMessageService {

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

        presenter = MessagePresenter(this, this, mCompositeDisposable)
        presenter.getCommentReplies(parentComment.post_id.toString())
    }

    private fun initView() {
        recycler_view_message.layoutManager = LinearLayoutManager(this)
        recycler_view_message.addItemDecoration(RecyclerViewMargin())

        button_send_comment.setOnClickListener {
            if (!isSubmittingComment) {
                val newComment = input_comment.text.toString()
                val spec = CommentSpec(parentComment.post_id.toString(), newComment)
                presenter.postReplyComment(spec)
            }
        }
    }

    private fun getParam() {
        val bundle = intent.getBundleExtra("bundle")
        parentComment = bundle.getParcelable("selectedComment") as Comment
    }

    private fun invalidateData(response: CommentResponse) {
        if (response.data.isNotEmpty()) {
            comments = response.data
            adapter = MessageAdapter()
            adapter.setMessages(comments)
            recycler_view_message.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun updateData(newData: List<Comment>) {
        val oldData = adapter.getItems()
        val result = DiffUtil.calculateDiff(MessageDiffUtilCallback(oldData, newData))
        adapter.setMessages(newData)
        result.dispatchUpdatesTo(adapter)
    }

    override fun showLoading() {}

    override fun hideLoading(statusCode: Int) {}

    override fun onDataResponse(data: CommentResponse) {
        invalidateData(data)
    }

    override fun onMessageBeingProcessed() {
        isSubmittingComment = true
    }

    override fun onMessageNotSent() {
        isSubmittingComment = false
    }

    override fun onMessageSent() {
        isSubmittingComment = false

        //TODO Update new comment from new user
        val newComment = parentComment.copy(
                content = parentComment.post_id.toString(),
                created_at = OAppUtil.generateFormatDateFromTimestamp(System.currentTimeMillis())
        )

        comments.add(newComment)

        updateData(comments)
    }

}
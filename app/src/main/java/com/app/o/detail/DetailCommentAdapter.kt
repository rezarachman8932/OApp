package com.app.o.detail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.app.o.R
import com.app.o.api.comment.Comment
import com.app.o.shared.OAppImageUtil
import com.app.o.shared.OAppUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_comment.*

class DetailCommentAdapter(var context: Context) : RecyclerView.Adapter<DetailCommentAdapter.ViewHolder>() {

    private lateinit var dataItems: List<Comment>
    private lateinit var listener: (Comment) -> Unit

    fun setData(data: List<Comment>) {
        dataItems = data
    }

    fun setListener(adapterListener: (Comment) -> Unit) {
        listener = adapterListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_list_comment, parent, false) as RelativeLayout
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return dataItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(context, dataItems[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(context: Context, item: Comment, listener: (Comment) -> Unit) {
            item_text_comment_name.text = item.username
            item_text_comment_content.text = item.content
            item_text_time_post.text = OAppUtil.getTimeAgo(OAppUtil.generateStringToTimestamp(item.created_at))

            if (item.reply_count!! > 0) {
                item_text_comment_replies.visibility = View.VISIBLE
                item_text_comment_replies.text = context.resources.getQuantityString(R.plurals.numberOfReplies, item.reply_count, item.reply_count)
            } else {
                item_text_comment_replies.visibility = View.GONE
            }

            OAppImageUtil.setImage(item.avatar, R.drawable.ic_logo, item_image_comment_avatar)

            containerView.setOnClickListener { listener(item) }
        }
    }

}
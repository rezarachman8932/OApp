package com.app.o.message

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.o.R
import com.app.o.api.comment.Comment
import com.app.o.shared.OAppUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_comment_ours.*
import kotlinx.android.synthetic.main.item_list_comment_theirs.*

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    companion object {
        const val TYPE_OURS   = 1
        const val TYPE_THEIRS = 2
    }

    private var dataItems: List<Comment> = arrayListOf()

    fun setMessages(data: List<Comment>) {
        dataItems = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            TYPE_OURS -> {
                ViewHolderOurs(inflater.inflate(R.layout.item_list_comment_ours, null))
            } else -> {
                ViewHolderTheirs(inflater.inflate(R.layout.item_list_comment_theirs, null))
            }
        }
    }

    override fun getItemCount(): Int {
        return dataItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataItems[position].username.equals(OAppUtil.getUserName(), false)) {
            TYPE_OURS
        } else {
            TYPE_THEIRS
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder.itemViewType) {
            TYPE_OURS -> {
                val ours = holder as ViewHolderOurs
                ours.bindItem(dataItems[position])
            }
            TYPE_THEIRS -> {
                val theirs = holder as ViewHolderTheirs
                theirs.bindItem(dataItems[position])
            }
        }
    }

    inner class ViewHolderOurs(override val containerView: View) : ViewHolder(containerView), LayoutContainer {
        fun bindItem(item: Comment) {
            item_text_comment_ours.text = item.content
            item_text_comment_ours_time.text = item.created_at
        }
    }

    inner class ViewHolderTheirs(override val containerView: View) : ViewHolder(containerView), LayoutContainer {
        fun bindItem(item: Comment) {
            item_text_comment_theirs.text = item.content
            item_text_comment_theirs_time.text = OAppUtil.getTimeAgo(OAppUtil.generateStringToTimestamp(item.created_at))
        }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
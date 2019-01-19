package com.app.o.message

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.app.o.api.comment.Comment
import kotlinx.android.extensions.LayoutContainer

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private lateinit var dataItems: List<Comment>

    fun setMessages(data: List<Comment>) {
        dataItems = data
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(item: Comment, listener: (Comment) -> Unit) {

        }
    }

}
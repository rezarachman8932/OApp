package com.app.o.user.blocked

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.o.R
import com.app.o.api.relation.UserConnected
import com.app.o.shared.util.OAppMultimediaUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_blocked_user.*

class BlockedAccountAdapter(var context: Context) : RecyclerView.Adapter<BlockedAccountAdapter.ViewHolder>() {

    private lateinit var dataItems: MutableList<UserConnected>
    private lateinit var listener: (Int, UserConnected) -> Unit

    fun setData(data: MutableList<UserConnected>) {
        dataItems = data
    }

    fun setListener(index: (Int, UserConnected) -> Unit) {
        listener = index
    }

    fun removeItem(position: Int) {
        dataItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun getCurrentDataList(): MutableList<UserConnected> {
        return dataItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_list_blocked_user, parent, false) as CardView
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return dataItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(position, dataItems[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(index: Int, blockedUser: UserConnected, listener: (Int, UserConnected) -> Unit) {
            OAppMultimediaUtil.setImage(blockedUser.avatar, R.drawable.ic_logo, item_image_blocked_user)

            item_text_blocked_name.text = blockedUser.name
            item_text_label_unblock.setOnClickListener { listener(index, blockedUser) }
        }
    }

}
package com.app.o.user.connected

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
import kotlinx.android.synthetic.main.item_list_connected_users.*

class ConnectedUsersAdapter(var context: Context) : RecyclerView.Adapter<ConnectedUsersAdapter.ViewHolder>() {

    private lateinit var dataItems: List<UserConnected>
    private lateinit var listener: (UserConnected) -> Unit

    fun setData(data: List<UserConnected>) {
        dataItems = data
    }

    fun setListener(adapterListener: (UserConnected) -> Unit) {
        listener = adapterListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_list_connected_users, parent, false) as CardView
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return dataItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(context, dataItems[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(context: Context, userConnected: UserConnected, listener: (UserConnected) -> Unit) {
            item_text_connected_username.text = userConnected.username

            if (userConnected.is_starred) {
                item_text_connected_verified.visibility = View.VISIBLE
                item_text_connected_verified.text = context.resources.getString(R.string.text_label_verified)
            } else {
                item_text_connected_verified.visibility = View.GONE
            }

            OAppMultimediaUtil.setImage(userConnected.avatar, R.drawable.ic_logo, item_image_connected_user)

            containerView.setOnClickListener { listener(userConnected) }
        }
    }

}
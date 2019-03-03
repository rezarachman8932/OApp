package com.app.o.notification.page

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.o.R
import com.app.o.api.notification.PushNotificationItem
import com.app.o.shared.util.OAppUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_notifications.*

class NotificationListAdapter : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {

    private lateinit var dataItems: List<PushNotificationItem>
    private lateinit var listener: (PushNotificationItem) -> Unit

    fun setData(data: List<PushNotificationItem>) {
        dataItems = data
    }

    fun setListener(adapterListener: (PushNotificationItem) -> Unit) {
        listener = adapterListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_list_notifications, parent, false) as CardView
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return dataItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(dataItems[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(item: PushNotificationItem, listener: (PushNotificationItem) -> Unit) {
            if (item.created_at != null) {
                item_text_notification_time.text = OAppUtil.getTimeAgo(OAppUtil.generateStringToTimestamp(item.created_at))
            }

            item_text_notification_message.text = item.message

            containerView.setOnClickListener { listener(item) }
        }
    }

}
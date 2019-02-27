package com.app.o.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.app.o.R
import com.app.o.api.home.HomePostItem
import com.app.o.shared.util.OAppMultimediaUtil
import com.app.o.shared.util.OAppUtil
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_photo.*

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private lateinit var dataItems: List<HomePostItem>
    private lateinit var listener: (HomePostItem) -> Unit

    fun setData(data: List<HomePostItem>) {
        dataItems = data
    }

    fun setListener(adapterListener: (HomePostItem) -> Unit) {
        listener = adapterListener
    }

    fun getItems(): List<HomePostItem> {
        return dataItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_list_photo, parent, false) as LinearLayout
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return dataItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(dataItems[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(item: HomePostItem, listener: (HomePostItem) -> Unit) {
            if (item.media_url.isNotEmpty()) {
                Picasso.get().load(item.media_url).into(item_image_thumbnail)
                item_image_thumbnail.visibility = View.VISIBLE
            } else {
                item_image_thumbnail.visibility = View.GONE
            }

            item_text_love_count.text = item.like_count.toString()

            if (item.is_liked) {
                item_icon_love_status.setImageResource(R.drawable.ic_vector_heart_green)
            } else {
                item_icon_love_status.setImageResource(R.drawable.ic_vector_heart_inactive)
            }

            when {
                item.type == OAppMultimediaUtil.TYPE_IMAGE -> item_icon_type_post.setImageResource(R.drawable.ic_type_bag)
                item.type == OAppMultimediaUtil.TYPE_TEXT -> item_icon_type_post.setImageResource(R.drawable.ic_vector_speaker)
                item.type == OAppMultimediaUtil.TYPE_VIDEO -> item_icon_type_post.setImageResource(R.drawable.ic_type_audio)
            }

            item_text_post_title.text = item.title
            item_text_post_username.text = item.username
            item_text_post_description.text = item.subtitle
            item_text_post_time.text = OAppUtil.getTimeAgo(OAppUtil.generateStringToTimestamp(item.created_at))
            item_text_comment_count.text = item.comment_count.toString()

            containerView.setOnClickListener { listener(item) }
        }
    }

}
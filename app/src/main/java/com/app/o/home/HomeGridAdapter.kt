package com.app.o.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.app.o.R
import com.app.o.api.home.HomePostItem
import com.app.o.shared.OAppUtil
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_grid_photo.*

class HomeGridAdapter(private val items: List<HomePostItem>, private val listener: (HomePostItem) -> Unit) : RecyclerView.Adapter<HomeGridAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_grid_photo, parent, false) as LinearLayout
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(item: HomePostItem, listener: (HomePostItem) -> Unit) {
            if (item.media_url.isNotEmpty()) {
                Picasso.get().load(item.media_url).into(item_image_thumbnail)
                item_image_thumbnail.visibility = View.VISIBLE
                item_separator_header.visibility = View.GONE
            } else {
                item_image_thumbnail.visibility = View.GONE
                item_separator_header.visibility = View.VISIBLE
            }

//            if (item.like_count > 0) {
//                item_layout_love.visibility = View.VISIBLE
                item_text_love_count.text = item.like_count.toString()
//            } else {
//                item_layout_love.visibility = View.GONE
//            }

            if (item.type == "image") {
                item_icon_type_post.setImageResource(R.drawable.ic_type_bag)
            } else if (item.type == "audio") {
                item_icon_type_post.setImageResource(R.drawable.ic_vector_speaker)
            }

            item_text_post_title.text = item.title
            item_text_post_description.text = item.subtitle
            item_text_post_time.text = OAppUtil.getTimeAgo(OAppUtil.generateStringToTimestamp(item.created_at))
            item_text_comment_count.text = item.comment_count.toString()

            containerView.setOnClickListener { listener(item) }
        }
    }

}
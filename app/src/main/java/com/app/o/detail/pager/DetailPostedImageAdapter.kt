package com.app.o.detail.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.app.o.api.media.MediaItem

class DetailPostedImageAdapter(fragmentManager: FragmentManager, private val images: List<MediaItem>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        return DetailPostedImageFragment.newInstance(images[position].url!!)
    }

    override fun getCount(): Int {
        return images.size
    }

}
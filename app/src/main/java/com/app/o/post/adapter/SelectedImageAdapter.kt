package com.app.o.post.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.app.o.post.multimedia.photo_video.SelectedImageFragment

class SelectedImageAdapter(fragmentManager: FragmentManager, private val uriList: List<String>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        return SelectedImageFragment.newInstance(uriList[position])
    }

    override fun getCount(): Int {
        return uriList.size
    }

}
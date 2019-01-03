package com.app.o.post.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.app.o.R
import com.app.o.post.multimedia.photo_video.PhotoVideoFragment
import com.app.o.post.multimedia.text.TextFragment

class PostPagerAdapter(fragmentManager: FragmentManager, val context: Context) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return PhotoVideoFragment.newInstance(position)
            1 -> return PhotoVideoFragment.newInstance(position)
            2 -> return TextFragment()
        }
        return null
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return context.getString(R.string.text_label_photo)
            1 -> return context.getString(R.string.text_label_video)
            2 -> return context.getString(R.string.text_label_text)
        }

        return ""
    }

}
package com.app.o.post

import android.os.Bundle
import com.app.o.R
import com.app.o.base.page.OAppActivity
import com.app.o.post.adapter.PostPagerAdapter
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : OAppActivity() {

    private lateinit var pagerAdapter: PostPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        supportActionBar?.title = getString(R.string.text_label_post)

        pagerAdapter = PostPagerAdapter(supportFragmentManager, this)
        view_pager_main.adapter = pagerAdapter
        tab_main.setupWithViewPager(view_pager_main)
    }

}
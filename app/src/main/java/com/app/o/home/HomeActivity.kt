package com.app.o.home

import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.app.o.R
import com.app.o.api.home.HomePostItem
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.custom.CountDrawable
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : OAppActivity(), OAppViewService<List<HomePostItem>> {

    private var count = 1
    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        text_home.setOnClickListener {
            count += 1
            invalidateOptionsMenu()
        }

        presenter = HomePresenter(this, mCompositeDisposable)
        presenter.getPostedTimeline("107.613795", "-6.881773")
    }

    override fun showLoading() {

    }

    override fun hideLoading(statusCode: Int) {

    }

    override fun onDataResponse(data: List<HomePostItem>) {
        if (data.isNotEmpty()) {
            Log.d("RESULT >>>", data[0].title)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_search -> {
            true
        }

        R.id.action_friend -> {
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        setCount(this, count.toString(), menu)
        return true
    }

    private fun setCount(context: Context, count: String, homeMenu: Menu) {
        val menuItem = homeMenu.findItem(R.id.action_friend)
        val icon = menuItem.icon as LayerDrawable

        val badge: CountDrawable
        val reuse = icon.findDrawableByLayerId(R.id.ic_group_count)

        badge = if (reuse != null && reuse is CountDrawable) {
            reuse
        } else {
            CountDrawable(context)
        }

        badge.setCount(count)
        icon.mutate()
        icon.setDrawableByLayerId(R.id.ic_group_count, badge)
    }

}

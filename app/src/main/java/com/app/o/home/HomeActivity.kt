package com.app.o.home

import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.app.o.R
import com.app.o.api.home.HomePostItem
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.custom.CountDrawable
import com.app.o.custom.GridSpacingItemDecoration
import com.app.o.shared.OAppUtil
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : OAppActivity(), OAppViewService<List<HomePostItem>> {

    private var count = 1
    private lateinit var presenter: HomePresenter
    private lateinit var adapter: HomeGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        text_home.setOnClickListener {
//            count += 1
//            invalidateOptionsMenu()
//        }

        initGrid()
        presenter = HomePresenter(this, mCompositeDisposable)
        requestCurrentLocation()
    }

    override fun onDestroy() {
        super.onDestroy()

        removeUpdateLocation()
    }

    override fun onLocationUpdated(location: Location) {
        super.onLocationUpdated(location)

        val longitude = location.longitude.toString()
        val latitude = location.latitude.toString()

        presenter.saveLastLocation(longitude, latitude)
        presenter.getPostedTimeline(longitude, latitude)
    }

    override fun showLoading() {

    }

    override fun hideLoading(statusCode: Int) {

    }

    override fun onDataResponse(data: List<HomePostItem>) {
        if (data.isNotEmpty()) {
            adapter = HomeGridAdapter(data, listener = {})
            recycler_view.adapter = adapter
            adapter.notifyDataSetChanged()
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

    private fun initGrid() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(GridSpacingItemDecoration(
                2,
                OAppUtil.dpToPx(this, 10),
                true))
        recycler_view.itemAnimator = DefaultItemAnimator()
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

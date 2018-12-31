package com.app.o.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.SearchView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.app.o.R
import com.app.o.api.home.HomeResponseZip
import com.app.o.api.location.LocationSpec
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.custom.BottomMenuView
import com.app.o.post.PostActivity
import com.app.o.shared.OAppUtil
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : OAppActivity(), OAppViewService<HomeResponseZip> {

    private var connectedCount = 0
    private lateinit var presenter: HomePresenter
    private lateinit var adapter: HomeGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initGrid()
        initBottomView()

        presenter = HomePresenter(this, mCompositeDisposable)

        requestCurrentLocation()
    }

    override fun onPause() {
        super.onPause()

        removeUpdateLocation()
    }

    override fun onLocationUpdated(location: Location) {
        super.onLocationUpdated(location)

        val longitude = location.longitude.toString()
        val latitude = location.latitude.toString()
        val locationSpec = LocationSpec(latitude, longitude)

        presenter.saveLastLocation(longitude, latitude)
        presenter.getPostedTimeline(locationSpec)
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
        layout_content_view.visibility = View.INVISIBLE
    }

    override fun hideLoading(statusCode: Int) {
        progress_bar.visibility = View.GONE
        layout_content_view.visibility = View.VISIBLE
    }

    override fun onDataResponse(data: HomeResponseZip) {
        connectedCount = data.userConnectedCount.amount
        invalidateOptionsMenu()

        if (isSuccess(data.homeResponse.status)) {
            if (data.homeResponse.data.isNotEmpty()) {
                adapter = HomeGridAdapter(data.homeResponse.data, listener = {})
                recycler_view.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)

        setSearchView(menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_friend -> {
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.findItem(R.id.action_friend)
        val icon = menuItem.icon as LayerDrawable

        OAppUtil.setIconCount(this, connectedCount.toString(), icon, R.id.ic_group_count)

        return true
    }

    private fun initGrid() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recycler_view.layoutManager = layoutManager
        recycler_view.setHasFixedSize(true)
        recycler_view.itemAnimator = DefaultItemAnimator()
    }

    private fun initBottomView() {
        bottom_menu.isShowBadge = true
        bottom_menu.imageURL = "http://api.ademuhammad.or.id/uploads/post/sunday-sale201812220713180.JPG"
        bottom_menu.setMenuListener { _, type ->
            when (type) {
                BottomMenuView.MESSAGE -> {}

                BottomMenuView.POST -> {
                    val intent = Intent(this, PostActivity::class.java)
                    startActivity(intent)
                }

                BottomMenuView.PROFILE -> {}
            }
        }
    }

    private fun setSearchView(menu: Menu?) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.onActionViewCollapsed()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

}

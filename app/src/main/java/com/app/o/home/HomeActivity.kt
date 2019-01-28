package com.app.o.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.location.Location
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.SearchView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.app.o.R
import com.app.o.api.home.HomePostItem
import com.app.o.api.home.HomeResponse
import com.app.o.api.home.HomeResponseZip
import com.app.o.api.location.LocationWithQuerySpec
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppSearchService
import com.app.o.base.service.OAppViewService
import com.app.o.custom.BottomMenuView
import com.app.o.detail.DetailActivity
import com.app.o.post.PostActivity
import com.app.o.shared.OAppUtil
import com.app.o.user.connected.ConnectedUsersActivity
import com.app.o.user.detail.UserProfileActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : OAppActivity(), OAppViewService<HomeResponseZip>, OAppSearchService {

    private var connectedCount = 0
    private var shouldLoadDefaultData: Boolean = false
    private var isFirstTimeLoad: Boolean = true

    private lateinit var presenter: HomePresenter
    private lateinit var adapter: HomeGridAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initGrid()
        initBottomView()

        presenter = HomePresenter(this, this, mCompositeDisposable)
    }

    override fun onResume() {
        super.onResume()

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

        presenter.saveLastLocation(longitude, latitude)
        presenter.getPostedTimeline(OAppUtil.generateLocationSpec(longitude, latitude))
    }

    override fun showLoading() {
        setDataListVisibility(false)
    }

    override fun hideLoading(statusCode: Int) {
        setDataListVisibility(true)
    }

    override fun onDataResponse(data: HomeResponseZip) {
        connectedCount = data.userConnectedCount.amount
        invalidateOptionsMenu()
        setData(data.homeResponse.data, data.homeResponse.status)
    }

    override fun onQueryProcessed() {
        setDataListVisibility(false)
    }

    override fun onQueryFailed() {
        setDataListVisibility(true)
        searchView.onActionViewCollapsed()
        presenter.getPostedTimeline(OAppUtil.generateLocationSpec(OAppUtil.getLongitude()!!, OAppUtil.getLatitude()!!))
    }

    override fun onQueryCompleted(response: HomeResponse) {
        setDataListVisibility(true)
        updateData(response.data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        setSearchView(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_friend -> {
            val intent = Intent(this, ConnectedUsersActivity::class.java)
            startActivity(intent)
            true
        } else -> {
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

                BottomMenuView.PROFILE -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setSearchView(menu: Menu?) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnCloseListener {
            if (shouldLoadDefaultData) {
                if (searchView.query.isEmpty()) {
                    shouldLoadDefaultData = false

                    presenter.getPostedTimeline(OAppUtil.generateLocationSpec(OAppUtil.getLongitude()!!, OAppUtil.getLatitude()!!))
                }
            }

            false
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                shouldLoadDefaultData = true

                val locationWithQuerySpec = LocationWithQuerySpec(query, OAppUtil.getLatitude()!!, OAppUtil.getLongitude()!!)
                presenter.getSearchPost(locationWithQuerySpec)

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun setDataListVisibility(showed: Boolean) {
        if (showed) {
            progress_bar.visibility = View.GONE
            layout_content_view.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.VISIBLE
            layout_content_view.visibility = View.INVISIBLE
        }
    }

    private fun setData(data: List<HomePostItem>, status: Int) {
        if (isSuccess(status)) {
            if (data.isNotEmpty()) {
                if (isFirstTimeLoad) {
                    adapter = HomeGridAdapter()
                    adapter.setData(data)
                    adapter.setListener {
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("postId", it.post_id.toString())
                        startActivity(intent)
                    }

                    recycler_view.adapter = adapter
                    adapter.notifyDataSetChanged()

                    isFirstTimeLoad = false
                } else {
                    updateData(data)
                }
            } else {
                //TODO Show empty state
            }
        }
    }

    private fun updateData(newData: List<HomePostItem>) {
        val oldData = adapter.getItems()
        val result = DiffUtil.calculateDiff(HomeDiffUtilCallback(oldData, newData))
        adapter.setData(newData)
        result.dispatchUpdatesTo(adapter)
    }

}

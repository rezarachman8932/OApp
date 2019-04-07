package com.app.o.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.location.Location
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.app.o.R
import com.app.o.api.home.HomePostItem
import com.app.o.api.home.HomeResponse
import com.app.o.api.home.HomeResponseZip
import com.app.o.api.location.LocationWithQuerySpec
import com.app.o.api.user.profile.UserProfileResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppSearchService
import com.app.o.base.service.OAppViewService
import com.app.o.custom.BottomMenuView
import com.app.o.detail.DetailActivity
import com.app.o.notification.page.NotificationListActivity
import com.app.o.post.PostActivity
import com.app.o.setting.SettingActivity
import com.app.o.shared.util.OAppNotificationUtil
import com.app.o.shared.util.OAppUtil
import com.app.o.user.connected.ConnectedUsersActivity
import com.app.o.user.detail.UserProfileActivity
import com.app.o.user.login.LoginActivity
import com.app.o.user.logout.LogoutCallback
import com.app.o.user.update_profile.UpdateProfileActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : OAppActivity(), OAppViewService<HomeResponseZip>, OAppSearchService, LogoutCallback {

    private var connectedCount = 0
    private var shouldLoadDefaultData: Boolean = false
    private var isFirstTimeLoad: Boolean = true

    private lateinit var presenter: HomePresenter
    private lateinit var adapter: HomeAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initSwipeRefresh()
        initGrid()
        initBottomView()

        presenter = HomePresenter(this, this, this, mCompositeDisposable)
    }

    override fun onStart() {
        super.onStart()

        initSignInGoogle()
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
        layout_root_home.isRefreshing = false

        if (statusCode == OAppUtil.ON_FINISH_FAILED) {
            showSnackBar(layout_root_home, getString(R.string.text_error_get_posted_list))
        }
    }

    override fun onDataResponse(data: HomeResponseZip) {
        connectedCount = data.userConnectedCount.amount
        invalidateOptionsMenu()
        updateBottomView(data.userProfileResponse)
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

    override fun onLogoutSucceed() {
        shouldShowProgress(false)
        removeUserState()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLogoutFailed() {
        shouldShowProgress(false)
        showSnackBar(layout_root_home, getString(R.string.text_error_fail_to_logout))
    }

    override fun onLogoutProceed() {
        shouldShowProgress(true)
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
        }

        R.id.action_sub_menu_setting -> {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            true
        }

        R.id.action_sub_menu_update_profile -> {
            val intent = Intent(this, UpdateProfileActivity::class.java)
            startActivity(intent)
            true
        }

        R.id.action_sub_menu_logout -> {
            logoutThirdPartyState(presenter)
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

    private fun initSwipeRefresh() {
        layout_root_home.setColorSchemeResources(R.color.colorGreen)
        layout_root_home.setOnRefreshListener {
            requestCurrentLocation()
        }
    }

    private fun initGrid() {
        val layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager
        recycler_view.setHasFixedSize(true)
        recycler_view.itemAnimator = DefaultItemAnimator()
    }

    private fun initBottomView() {
        bottom_menu.setMenuListener { _, type ->
            when (type) {
                BottomMenuView.MESSAGE -> {
                    val intent = Intent(this, NotificationListActivity::class.java)
                    startActivity(intent)
                }

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
                val filteredData : MutableList<HomePostItem> = mutableListOf()

                data.forEach {
                    if (!it.is_blocked_user) {
                        filteredData.add(it)
                    }
                }

                if (isFirstTimeLoad) {
                    adapter = HomeAdapter(this)
                    adapter.setData(filteredData)
                    adapter.setListener {
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra(POST_ID, it.post_id.toString())
                        intent.putExtra(USER_ID, it.user_id)
                        startActivity(intent)
                    }

                    recycler_view.adapter = adapter
                    adapter.notifyDataSetChanged()

                    isFirstTimeLoad = false
                } else {
                    updateData(filteredData)
                }

                layout_group_home_empty_state.visibility = View.GONE
            } else {
                layout_group_home_empty_state.visibility = View.VISIBLE
            }
        }
    }

    private fun updateBottomView(userProfileResponse: UserProfileResponse) {
        bottom_menu.isShowBadge = OAppNotificationUtil.isPushNotificationExist()

        if (!userProfileResponse.avatar.isNullOrEmpty()) {
            bottom_menu.imageURL = userProfileResponse.avatar
        }
    }

    private fun updateData(newData: List<HomePostItem>) {
        val oldData = adapter.getItems()
        val result = DiffUtil.calculateDiff(HomeDiffUtilCallback(oldData, newData))
        adapter.setData(newData)
        result.dispatchUpdatesTo(adapter)
    }

}

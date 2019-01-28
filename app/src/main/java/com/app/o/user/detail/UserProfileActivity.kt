package com.app.o.user.detail

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MenuItem
import com.app.o.R
import com.app.o.api.user.UserProfileResponseZip
import com.app.o.api.user.UserProfileSpec
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.home.HomeGridAdapter
import com.app.o.shared.OAppImageUtil
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserProfileActivity : OAppActivity(), OAppViewService<UserProfileResponseZip> {

    private lateinit var presenter: UserProfilePresenter
    private lateinit var adapter: HomeGridAdapter
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        getParam()

        initView()

        presenter = UserProfilePresenter(this, mCompositeDisposable)
        presenter.getProfile(UserProfileSpec(userId))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading(statusCode: Int) {

    }

    override fun onDataResponse(data: UserProfileResponseZip) {
        OAppImageUtil.setImage("http://api.ademuhammad.or.id/uploads/post/sunday-sale201812220713180.JPG", R.drawable.ic_logo, image_user_detail_profile)

        text_user_detail_name.text = data.userProfileResponse.name
        text_user_location_name.text = data.userProfileResponse.location
        text_user_detail_additional_info.text = data.userProfileResponse.website

        if (data.homeResponse.data.isNotEmpty()) {
            adapter = HomeGridAdapter()
            adapter.setData(data.homeResponse.data)
            adapter.setListener {}

            recycler_view_posted_user.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.text_label_header_user_profile)

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recycler_view_posted_user.layoutManager = layoutManager
        recycler_view_posted_user.setHasFixedSize(true)
        recycler_view_posted_user.itemAnimator = DefaultItemAnimator()
    }

    private fun getParam() {
        userId = intent.getIntExtra("userId", -1)
    }

}
package com.app.o.user.detail

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MenuItem
import android.view.View
import com.app.o.R
import com.app.o.api.user.profile.UserProfileResponseZip
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.home.HomeAdapter
import com.app.o.shared.util.OAppMultimediaUtil
import com.app.o.shared.util.OAppUtil
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserProfileActivity : OAppActivity(), OAppViewService<UserProfileResponseZip> {

    private lateinit var presenter: UserProfilePresenter
    private lateinit var adapter: HomeAdapter
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        getParam()

        initView()

        presenter = UserProfilePresenter(this, mCompositeDisposable)
        presenter.getProfile(userId)
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
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading(statusCode: Int) {
        progress_bar.visibility = View.GONE

        if (statusCode == OAppUtil.ON_FINISH_FAILED) {
            finish()
        }
    }

    override fun onDataResponse(data: UserProfileResponseZip) {
        val profileResponse = data.userProfileResponse

        OAppMultimediaUtil.setImage(profileResponse.avatar, R.drawable.ic_logo, image_user_detail_profile)

        text_user_detail_name.text = profileResponse.name

        if (!profileResponse.location.isNullOrEmpty()) {
            text_user_location_name.text = profileResponse.location
        }

        if (!profileResponse.website.isNullOrEmpty()) {
            text_user_detail_additional_info.text = profileResponse.website
        }

        if (data.homeResponse.data.isNotEmpty()) {
            adapter = HomeAdapter()
            adapter.setData(data.homeResponse.data)
            adapter.setListener {}

            recycler_view_posted_user.adapter = adapter
            adapter.notifyDataSetChanged()

            layout_group_profile_user_post.visibility = View.VISIBLE
        } else {
            text_empty_state_have_no_posted.visibility = View.VISIBLE
        }

        layout_group_scroll_content.visibility = View.VISIBLE
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
        userId = intent.getIntExtra(USER_ID, 0)
    }

}
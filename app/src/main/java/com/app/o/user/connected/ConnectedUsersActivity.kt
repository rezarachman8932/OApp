package com.app.o.user.connected

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.app.o.R
import com.app.o.api.relation.UserConnectedResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.custom.RecyclerViewMargin
import com.app.o.shared.OAppUtil
import com.app.o.user.detail.UserProfileActivity
import kotlinx.android.synthetic.main.activity_connected_users.*

class ConnectedUsersActivity : OAppActivity(), OAppViewService<UserConnectedResponse> {

    private lateinit var presenter: ConnectedUsersPresenter
    private lateinit var adapter: ConnectedUsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connected_users)

        initView()

        presenter = ConnectedUsersPresenter(this, mCompositeDisposable)
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
        presenter.getConnectedUsers(OAppUtil.generateLocationSpec(longitude, latitude))
    }

    override fun showLoading() {}

    override fun hideLoading(statusCode: Int) {}

    override fun onDataResponse(data: UserConnectedResponse) {
        if (data.data.isNotEmpty()) {
            text_header_connected_users.text = resources.getQuantityString(R.plurals.numberOfConnectedUsers, data.data.size, data.data.size)

            adapter = ConnectedUsersAdapter(this)
            adapter.setData(data.data)
            adapter.setListener {
                val intent = Intent(this, UserProfileActivity::class.java)
                intent.putExtra("userId", it.user_id)
                startActivity(intent)
            }

            recycler_view_connected_users.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun initView() {
        supportActionBar?.title = getString(R.string.text_label_people_around_you)

        recycler_view_connected_users.layoutManager = LinearLayoutManager(this)
        recycler_view_connected_users.addItemDecoration(RecyclerViewMargin(RecyclerViewMargin.DP_16))
    }

}
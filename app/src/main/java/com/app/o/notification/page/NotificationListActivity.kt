package com.app.o.notification.page

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.app.o.R
import com.app.o.api.notification.PushNotificationItem
import com.app.o.api.notification.PushNotificationReadSpec
import com.app.o.api.notification.PushNotificationResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.custom.RecyclerViewMargin
import com.app.o.detail.DetailActivity
import com.app.o.shared.util.OAppNotificationUtil
import com.app.o.shared.util.OAppUserUtil
import com.app.o.shared.util.OAppUtil
import kotlinx.android.synthetic.main.activity_notification_list.*

class NotificationListActivity : OAppActivity(), OAppViewService<PushNotificationResponse>, NotificationListCallback {

    private lateinit var presenter: NotificationListPresenter
    private lateinit var adapter: NotificationListAdapter
    private lateinit var selectedPushNotificationItem: PushNotificationItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)

        initView()

        presenter = NotificationListPresenter(this, this, mCompositeDisposable)
    }

    override fun onResume() {
        super.onResume()

        presenter.getPushNotificationList()
    }

    override fun showLoading() {
        shouldShowProgress(true)
    }

    override fun hideLoading(statusCode: Int) {
        shouldShowProgress(false)

        if (statusCode == OAppUtil.ON_FINISH_FAILED) {
            showSnackBar(layout_root_notification_list, getString(R.string.text_error_get_notification_list))
        }
    }

    override fun onDataResponse(data: PushNotificationResponse) {
        if (isSuccess(data.status)) {
            val notifications: MutableList<PushNotificationItem> = arrayListOf()
            var readNotification = 0

            data.data.forEach {
                if (it.user_id != OAppUserUtil.getUserId()) {
                    notifications.add(it)
                }
            }

            adapter = NotificationListAdapter()
            adapter.setData(notifications)
            adapter.setListener {
                if (it.is_read) {
                    goToNotificationDetailPage(it)
                } else {
                    selectedPushNotificationItem = it
                    presenter.setPushNotificationAsRead(PushNotificationReadSpec(it.notification_id))
                }
            }

            recycler_view_notification_list.adapter = adapter
            adapter.notifyDataSetChanged()

            notifications.forEach {
                if (it.is_read) {
                    readNotification += 1
                }
            }

            if (readNotification == notifications.size) {
                OAppNotificationUtil.setPushNotificationExist(false)
                OAppNotificationUtil.clearNotifications(this)
            }

            progress_bar.visibility = View.GONE
            recycler_view_notification_list.visibility = View.VISIBLE
        }
    }

    override fun onSetAsReadProgress() {
        shouldShowProgress(true)
    }

    override fun onSetAsReadComplete(statusCode: Int) {
        shouldShowProgress(false)

        if (statusCode == OAppUtil.ON_FINISH_SUCCEED) {
            goToNotificationDetailPage(selectedPushNotificationItem)
        } else {
            showSnackBar(layout_root_notification_list, getString(R.string.text_error_set_notification_as_read))
        }
    }

    private fun initView() {
        supportActionBar?.title = getString(R.string.text_notification_list_header)

        recycler_view_notification_list.layoutManager = LinearLayoutManager(this)
        recycler_view_notification_list.addItemDecoration(RecyclerViewMargin(RecyclerViewMargin.DP_16))
    }

    private fun goToNotificationDetailPage(pushNotificationItem: PushNotificationItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(POST_ID, pushNotificationItem.ref_id.toString())
        intent.putExtra(USER_ID, pushNotificationItem.user_id)
        startActivity(intent)
    }

}
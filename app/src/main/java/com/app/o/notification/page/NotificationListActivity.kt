package com.app.o.notification.page

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.app.o.R
import com.app.o.api.notification.PushNotificationItem
import com.app.o.api.notification.PushNotificationResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.custom.RecyclerViewMargin
import com.app.o.shared.util.OAppNotificationUtil
import com.app.o.shared.util.OAppUserUtil
import kotlinx.android.synthetic.main.activity_notification_list.*

class NotificationListActivity : OAppActivity(), OAppViewService<PushNotificationResponse> {

    private lateinit var presenter: NotificationListPresenter
    private lateinit var adapter: NotificationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)

        initView()

        presenter = NotificationListPresenter(this, mCompositeDisposable)
    }

    override fun onResume() {
        super.onResume()

        presenter.getPushNotificationList()
    }

    override fun showLoading() {}

    override fun hideLoading(statusCode: Int) {}

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
                //TODO Go to destination page based on action_type
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

    private fun initView() {
        supportActionBar?.title = getString(R.string.text_notification_list_header)

        recycler_view_notification_list.layoutManager = LinearLayoutManager(this)
        recycler_view_notification_list.addItemDecoration(RecyclerViewMargin(RecyclerViewMargin.DP_16))
    }

}
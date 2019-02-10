package com.app.o.user.blocked

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.app.o.R
import com.app.o.api.user.blocked.BlockedUserResponse
import com.app.o.api.user.blocked.UnblockedUserResponse
import com.app.o.api.user.blocked.UnblockedUserSpec
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.custom.RecyclerViewMargin
import kotlinx.android.synthetic.main.activity_blocked_account.*

class BlockedAccountActivity : OAppActivity(), OAppViewService<BlockedUserResponse>, UnblockedAccountCallback {

    private lateinit var presenter: BlockedAccountPresenter
    private lateinit var adapter: BlockedAccountAdapter
    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_account)

        initView()

        presenter = BlockedAccountPresenter(this, this, mCompositeDisposable)
        presenter.getBlockedUsers()
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
        layout_group_empty_state.visibility = View.GONE
        recycler_view_blocked_users.visibility = View.GONE
    }

    override fun hideLoading(statusCode: Int) {
        progress_bar.visibility = View.GONE
    }

    override fun onDataResponse(data: BlockedUserResponse) {
        if (isSuccess(data.status)) {
            if (data.data.isNotEmpty()) {
                adapter = BlockedAccountAdapter(this)
                adapter.setData(data.data)
                adapter.setListener { position, user ->
                    index = position
                    presenter.unBlockedUser(UnblockedUserSpec(user.user_id))
                }

                recycler_view_blocked_users.adapter = adapter
                adapter.notifyDataSetChanged()

                recycler_view_blocked_users.visibility = View.VISIBLE
            } else {
                layout_group_empty_state.visibility = View.VISIBLE
            }
        }
    }

    override fun onUnblockingAccount() {
        shouldShowProgress(true)
    }

    override fun onUnblockedAccountSuccceed(response: UnblockedUserResponse) {
        if (isSuccess(response.status)) {
            shouldShowProgress(false)
            adapter.removeItem(index)

            if (adapter.getCurrentDataList().size < 1) {
                recycler_view_blocked_users.visibility = View.GONE
                layout_group_empty_state.visibility = View.VISIBLE
            }
        }
    }

    override fun onUnblockedAccountFailed() {
        shouldShowProgress(false)
        showSnackBar(layout_root_blocked_account, getString(R.string.text_error_fail_to_unblock_user))
    }

    private fun initView() {
        supportActionBar?.title = getString(R.string.text_label_setting_blocked_account)

        recycler_view_blocked_users.layoutManager = LinearLayoutManager(this)
        recycler_view_blocked_users.addItemDecoration(RecyclerViewMargin(RecyclerViewMargin.DP_16))
    }

}
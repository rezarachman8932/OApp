package com.app.o.setting

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import com.app.o.R
import com.app.o.shared.util.OAppUtil
import com.app.o.user.blocked.BlockedAccountActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initView()
    }

    private fun initView() {
        supportActionBar?.title = getString(R.string.text_header_setting)

        switch_notification.isChecked = OAppUtil.shouldReceiveNotification()
        switch_notification.setOnCheckedChangeListener { _, checked ->
            OAppUtil.setReceiveNotification(checked)
        }

        text_selected_distance.text = getString(R.string.text_label_distance_km, OAppUtil.getRangeFinder())
        text_range_finder_min.text = getString(R.string.text_label_distance_km, MIN_RANGE)
        text_range_finder_max.text = getString(R.string.text_label_distance_km, MAX_RANGE)

        seek_bar_range_finder.progress = OAppUtil.getRangeFinder()
        seek_bar_range_finder.incrementProgressBy(MIN_RANGE)
        seek_bar_range_finder.max = MAX_RANGE
        seek_bar_range_finder.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                OAppUtil.setRangeFinder(value)
                text_selected_distance.text = getString(R.string.text_label_distance_km, value)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        layout_group_blocked_account.setOnClickListener {
            val intent = Intent(this, BlockedAccountActivity::class.java)
            startActivity(intent)
        }

        layout_group_change_password.setOnClickListener {

        }

        layout_group_about.setOnClickListener {

        }
    }

    companion object {
        const val MIN_RANGE = 1
        const val MAX_RANGE = 20
    }

}
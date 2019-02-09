package com.app.o.setting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.app.o.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initView()
    }

    private fun initView() {
        supportActionBar?.title = getString(R.string.text_header_setting)

        switch_notification.isChecked = true
        switch_notification.setOnCheckedChangeListener { _, b ->
            if (b) {
                //TODO Set to preference
            }
        }
    }

}
package com.app.o.setting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.app.o.R

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initView()
    }

    private fun initView() {
        supportActionBar?.title = getString(R.string.text_header_setting)
    }

}
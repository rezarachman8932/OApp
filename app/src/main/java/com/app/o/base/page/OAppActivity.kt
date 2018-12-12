package com.app.o.base.page

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

abstract class OAppActivity : AppCompatActivity() {

    protected lateinit var mCompositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCompositeDisposable = CompositeDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()

        mCompositeDisposable.clear()
    }

}
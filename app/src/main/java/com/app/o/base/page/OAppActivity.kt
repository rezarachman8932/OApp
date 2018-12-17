package com.app.o.base.page

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import com.app.o.shared.OAppPreferencesHelper
import io.reactivex.disposables.CompositeDisposable
import pub.devrel.easypermissions.EasyPermissions

abstract class OAppActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    protected lateinit var mCompositeDisposable: CompositeDisposable
    private lateinit var preferencesHelper: OAppPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCompositeDisposable = CompositeDisposable()
        preferencesHelper = OAppPreferencesHelper(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        mCompositeDisposable.clear()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    protected fun showSnackBar(root: View, message: String) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
    }

    protected fun isSuccess(message: String, status:String): Boolean {
        return (message.equals("", true) && status.equals("", true))
    }

    protected fun getToken(): String? {
        return preferencesHelper.deviceToken
    }

    protected fun setToken(token: String) {
        preferencesHelper.deviceToken = token
    }

    protected fun setPasswordPreview(showPassword: Boolean, editText: EditText) {
        if (showPassword) {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

}
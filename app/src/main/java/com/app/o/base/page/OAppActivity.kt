package com.app.o.base.page

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import com.app.o.shared.OAppUtil
import io.reactivex.disposables.CompositeDisposable
import pub.devrel.easypermissions.EasyPermissions

abstract class OAppActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    protected lateinit var mCompositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCompositeDisposable = CompositeDisposable()
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

    protected fun isSuccess(status: Int): Boolean {
        return (status == OAppUtil.SUCCESS_STATUS)
    }

    protected fun setPasswordPreview(showPassword: Boolean, editText: EditText) {
        if (showPassword) {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

}
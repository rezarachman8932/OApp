package com.app.o.base.page

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import com.app.o.R
import com.app.o.shared.OAppUtil
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import io.reactivex.disposables.CompositeDisposable
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

abstract class OAppActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    protected lateinit var mCompositeDisposable: CompositeDisposable
    private lateinit var mLocationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCompositeDisposable = CompositeDisposable()

        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
        if (requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            openMedia()
        } else {
            requestCurrentLocation()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE -> {}
                OAppUtil.ON_ENABLE_GPS_SETTING -> requestCurrentLocation()
                PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                    if (data != null) {
                        val returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    }
                }
            }
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            onLocationUpdated(location)
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        val providers = mLocationManager.getProviders(false)
        var bestLocation: Location? = null

        for (provider in providers) {
            val loc = mLocationManager.getLastKnownLocation(provider) ?: continue

            if (bestLocation == null || loc.accuracy < bestLocation.accuracy) {
                bestLocation = loc
            }
        }

        return bestLocation
    }

    @SuppressLint("MissingPermission")
    protected fun requestCurrentLocation() {
        val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

        if (EasyPermissions.hasPermissions(this, *permissions)) {
            try {
                val location = getLastKnownLocation()

                if (location != null) {
                    locationListener.onLocationChanged(location)
                } else {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, OAppUtil.ON_ENABLE_GPS_SETTING)
                }

                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1f, locationListener)
            } catch (securityException: SecurityException) {
                securityException.message
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.text_label_dialog_request_dialog_title), 1, *permissions)
        }
    }

    protected fun removeUpdateLocation() {
        mLocationManager.removeUpdates(locationListener)
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

    protected fun openMedia() {
        Pix.start(this, PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
    }

    open fun onLocationUpdated(location: Location) {}

}
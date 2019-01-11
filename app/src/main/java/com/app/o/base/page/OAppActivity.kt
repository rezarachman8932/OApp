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
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.app.o.R
import com.app.o.shared.OAppUtil
import io.reactivex.disposables.CompositeDisposable
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

abstract class OAppActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    protected lateinit var mCompositeDisposable: CompositeDisposable

    private lateinit var mLocationManager: LocationManager
    private lateinit var mAlert: AlertDialog

    private val mPermissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

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
        requestCurrentLocation()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE -> {}
                OAppUtil.ON_ENABLE_GPS_SETTING -> requestCurrentLocation()
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
        if (EasyPermissions.hasPermissions(this, *mPermissions)) {
            try {
                val location = getLastKnownLocation()

                if (location != null) {
                    locationListener.onLocationChanged(location)
                } else {
                    //TODO Give an option for user either use last location or get new location
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, OAppUtil.ON_ENABLE_GPS_SETTING)
                }

                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, locationListener)
            } catch (securityException: SecurityException) {
                securityException.message
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.text_label_dialog_request_dialog_title), OAppUtil.REQUEST_CODE_FOR_LOCATION, *mPermissions)
        }
    }

    protected fun removeUpdateLocation() {
        mLocationManager.removeUpdates(locationListener)
    }

    protected fun showSnackBar(root: View, message: String) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
    }

    protected fun shouldShowProgress(showed: Boolean) {
        if (showed) {
            val loadingProgress = AlertDialog.Builder(this, R.style.CustomDialogTheme)
            with(loadingProgress) {
                setTitle(getString(R.string.text_loading_dialog_title))
                setMessage(getString(R.string.text_loading_dialog_subtitle))
            }

            mAlert = loadingProgress.create()
            with(mAlert) {
                show()
                window?.attributes
            }

            val title = mAlert.findViewById<TextView>(android.R.id.title)
            val message = mAlert.findViewById<TextView>(android.R.id.message)
            title?.textSize = 12f
            message?.textSize = 12f
        } else {
            mAlert.dismiss()
        }
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

    open fun onLocationUpdated(location: Location) {}

}
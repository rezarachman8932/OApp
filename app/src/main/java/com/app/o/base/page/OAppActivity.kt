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
import com.app.o.api.login.account.LoginResponse
import com.app.o.shared.util.OAppUserUtil
import com.app.o.shared.util.OAppUtil
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import io.reactivex.disposables.CompositeDisposable
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

abstract class OAppActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    protected lateinit var mCompositeDisposable: CompositeDisposable

    private lateinit var mLocationManager: LocationManager
    private lateinit var mAlert: AlertDialog

    private val mPermissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    companion object {
        private const val MIN_REFRESH_LOCATION: Long = 30000
        private const val MIN_DISTANCE:Float = 10f

        const val POST_ID = "postId"
        const val USER_ID = "userId"

        const val POSTED_ID = "posted_id"
        const val POSTED_TITLE = "posted_title"
        const val POSTED_SUBTITLE = "posted_subtitle"

        const val SELECTED_COMMENT = "selectedComment"
        const val BUNDLE = "bundle"
    }

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
                PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                    if (data != null) {
                        val returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                        onSuccessGetImage(returnValue)
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
        if (EasyPermissions.hasPermissions(this, *mPermissions)) {
            try {
                val location = getLastKnownLocation()

                if (location != null) {
                    locationListener.onLocationChanged(location)
                } else {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, OAppUtil.ON_ENABLE_GPS_SETTING)
                }

                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_REFRESH_LOCATION, MIN_DISTANCE, locationListener)
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

    protected fun saveUserState(data: LoginResponse) {
        OAppUserUtil.setToken(data.token)
        OAppUserUtil.setUserName(data.username)
        OAppUserUtil.setUserId(data.user_id)
        OAppUserUtil.setEmail(data.email)
        OAppUserUtil.setPhoneNumber(data.phonenumber)
        OAppUserUtil.setUserState(OAppUserUtil.USER_STATE_LOGGED_IN)
    }

    protected fun saveActivationUserTokenState(activationType: String) {
        OAppUserUtil.setUserState(OAppUserUtil.USER_STATE_REGISTRATION_NOT_COMPLETED)
        OAppUserUtil.setActivationType(activationType)
    }

    protected fun getDeviceToken(): String? {
        return OAppUserUtil.getFCMToken()
    }

    protected fun removeUserState() {
        OAppUserUtil.setUserState(OAppUserUtil.USER_STATE_NOT_LOGGED_IN)
    }

    protected fun openMedia() {
        Pix.start(this, PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
    }

    open fun onSuccessGetImage(values: ArrayList<String>) {}

    open fun onLocationUpdated(location: Location) {}

}
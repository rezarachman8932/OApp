package com.app.o.base.page

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ScrollView
import android.widget.TextView
import com.app.o.R
import com.app.o.shared.util.OAppUtil
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions

abstract class OAppFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    protected lateinit var mCompositeDisposable: CompositeDisposable
    private lateinit var mAlert: AlertDialog

    companion object {
        const val INDEX_IMAGE = 0
        const val INDEX_VIDEO = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCompositeDisposable = CompositeDisposable()
    }

    override fun onPause() {
        super.onPause()

        mCompositeDisposable.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                    if (data != null) {
                        val returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                        onSuccessGetImage(returnValue)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            openMedia()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        //TODO Force user to enable permission
    }

    private fun isSuccess(status: Int): Boolean {
        return (status == OAppUtil.SUCCESS_STATUS)
    }

    protected fun showSnackBar(message: String, root: ScrollView) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
    }

    protected fun closePage(status: Int) {
        if (isSuccess(status)) {
            activity?.supportFragmentManager?.popBackStack()
            activity?.finish()
        }
    }

    protected fun shouldShowProgress(showed: Boolean) {
        if (showed) {
            val loadingProgress = AlertDialog.Builder(context!!, R.style.CustomDialogTheme)
            with(loadingProgress) {
                setTitle(getString(R.string.text_loading_posting_dialog_title))
                setMessage(getString(R.string.text_loading_posting_dialog_subtitle))
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

    protected fun openMedia() {
        Pix.start(this, PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, OAppUtil.MAX_IMAGES_SELECTION_COUNT)
    }

    @NonNull
    protected fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString)
    }

    protected fun validateInput(scrollView: ScrollView, titleProduct: String, description: String, note: String): Boolean {
        if (titleProduct.isEmpty()) {
            showSnackBar(getString(R.string.text_label_title_is_empty), scrollView)
            return false
        }

        if (description.isEmpty()) {
            showSnackBar(getString(R.string.text_label_description_is_empty), scrollView)
            return false
        }

        if (note.isEmpty()) {
            showSnackBar(getString(R.string.text_label_note_is_empty), scrollView)
            return false
        }

        return true
    }

    open fun onSuccessGetImage(values: ArrayList<String>) {}

}
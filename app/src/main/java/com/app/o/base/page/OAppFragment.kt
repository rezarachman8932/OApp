package com.app.o.base.page

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.ScrollView
import com.app.o.R
import com.app.o.shared.OAppUtil
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


abstract class OAppFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    protected lateinit var mCompositeDisposable: CompositeDisposable

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

    private fun openMedia() {
        Pix.start(this, PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
    }

    private fun showSnackBar(message: String, root: ScrollView) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
    }

    protected fun isSuccess(status: Int): Boolean {
        return (status == OAppUtil.SUCCESS_STATUS)
    }

    @NonNull
    protected fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), descriptionString)
    }

    @NonNull
    protected fun prepareFilePart(partName: String, filePath: String): MultipartBody.Part {
        val file = File(filePath)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
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
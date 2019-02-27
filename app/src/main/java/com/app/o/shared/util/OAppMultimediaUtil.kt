package com.app.o.shared.util

import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Picasso
import io.reactivex.annotations.NonNull
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class OAppMultimediaUtil {

    companion object {
        const val TYPE_IMAGE = "image"
        const val TYPE_TEXT = "text"
        const val TYPE_VIDEO = "video"

        @NonNull
        fun prepareFileImagePart(key: String, bitmap: Bitmap?, filePath: String?): MultipartBody.Part {
            val file = createFileFromPath(bitmap, filePath)
            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            return MultipartBody.Part.createFormData(key, file.name, requestFile)
        }

        fun setImage(url: String?, holderSrc: Int?, imageView: ImageView) {
            if (!url.isNullOrEmpty() && holderSrc != null) {
                Picasso.get().load(url).placeholder(holderSrc).into(imageView)
            } else if (!url.isNullOrEmpty() && holderSrc == null) {
                Picasso.get().load(url).into(imageView)
            } else {
                Picasso.get().load(holderSrc!!).into(imageView)
            }
        }

        private fun createFileFromPath(bitmap: Bitmap?, path: String?): File {
            val file = File(path)
            val bos = ByteArrayOutputStream()

            bitmap?.compress(Bitmap.CompressFormat.WEBP, 0, bos)

            val bitmapData = bos.toByteArray()

            try {
                val fos = FileOutputStream(file)
                fos.write(bitmapData)
                fos.flush()
                fos.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

            return file
        }
    }

}
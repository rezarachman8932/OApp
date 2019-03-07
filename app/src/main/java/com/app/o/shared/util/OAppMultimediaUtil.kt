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
import android.media.MediaMetadataRetriever
import android.os.Build
import android.provider.MediaStore
import android.app.Activity
import android.content.Context
import android.net.Uri

class OAppMultimediaUtil {

    companion object {
        const val TYPE_IMAGE = "image"
        const val TYPE_TEXT = "text"
        const val TYPE_VIDEO = "video"

        const val REQUEST_CODE_PICK_VIDEO = 17

        @NonNull
        fun prepareFileImagePart(key: String, bitmap: Bitmap?, filePath: String?): MultipartBody.Part {
            val file = createFileFromPath(bitmap, filePath)
            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            return MultipartBody.Part.createFormData(key, file.name, requestFile)
        }

        @NonNull
        fun prepareFileVideoPart(key: String, videoPath: String): MultipartBody.Part {
            val file = File(videoPath)
            val requestFile = RequestBody.create(MediaType.parse("video/*"), file)
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

        fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "", null)
            return Uri.parse(path)
        }

        @Throws(Throwable::class)
        fun generateBitmapFromVideoFrame(videoPath: String): Bitmap {
            val bitmap: Bitmap
            var mediaMetadataRetriever: MediaMetadataRetriever? = null

            try {
                mediaMetadataRetriever = MediaMetadataRetriever()

                if (Build.VERSION.SDK_INT >= 14) {
                    mediaMetadataRetriever.setDataSource(videoPath, HashMap())
                } else {
                    mediaMetadataRetriever.setDataSource(videoPath)
                }

                bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST)
            } catch (e: Exception) {
                e.printStackTrace()

                throw Throwable(e.message)
            } finally {
                mediaMetadataRetriever?.release()
            }

            return bitmap
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

        fun getVideoPath(uri: Uri, activity: Activity): String? {
            val projection = arrayOf(MediaStore.Video.Media.DATA)
            val cursor = activity.contentResolver.query(uri, projection, null, null, null)

            return if (cursor != null) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                cursor.moveToFirst()
                cursor.getString(columnIndex)
            } else {
                uri.path
            }
        }
    }

}
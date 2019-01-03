package com.app.o.post.multimedia.photo_video

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import com.app.o.R
import com.app.o.api.post.CreatedPostResponse
import com.app.o.base.page.OAppFragment
import com.app.o.base.service.OAppViewService
import com.app.o.shared.OAppUtil
import kotlinx.android.synthetic.main.fragment_photo_video.*
import okhttp3.MultipartBody

class PhotoVideoFragment : OAppFragment(), OAppViewService<CreatedPostResponse> {

    private var index: Int? = -1
    private var bitmap: Bitmap? = null
    private var imagePath: String? = null

    private lateinit var imagePreview: ImageView
    private lateinit var backgroundImageLayout: RelativeLayout
    private lateinit var buttonPost: Button
    private lateinit var inputTitle: EditText
    private lateinit var inputDescription: EditText
    private lateinit var inputNote: EditText

    private lateinit var presenter: PhotoVideoPresenter

    companion object {
        private const val indexPage = "index"

        fun newInstance(index: Int): PhotoVideoFragment {
            val args = Bundle()
            args.putInt(indexPage, index)
            val fragment = PhotoVideoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        index = args?.getInt(indexPage)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_video, container, false)

        imagePreview = view.findViewById(R.id.image_preview_post) as ImageView

        inputTitle = view.findViewById(R.id.input_title_product) as EditText
        inputDescription = view.findViewById(R.id.input_description) as EditText
        inputNote = view.findViewById(R.id.input_note) as EditText

        backgroundImageLayout = view.findViewById(R.id.view_layout_multimedia) as RelativeLayout
        backgroundImageLayout.setOnClickListener { openMedia() }

        buttonPost = view.findViewById(R.id.button_post) as Button
        buttonPost.setOnClickListener { uploadData() }

        when (index) {
            0 -> imagePreview.setBackgroundResource(R.drawable.bg_default_post_image)
            1 -> imagePreview.setBackgroundResource(R.drawable.bg_default_post_video)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter = PhotoVideoPresenter(this, mCompositeDisposable)
    }

    override fun showLoading() {}

    override fun hideLoading(statusCode: Int) {}

    override fun onDataResponse(data: CreatedPostResponse) {
        closePage(data.status)
    }

    override fun onSuccessGetImage(values: ArrayList<String>) {
        super.onSuccessGetImage(values)

        imagePath = values[0]
        bitmap = BitmapFactory.decodeFile(imagePath)
        imagePreview.setImageBitmap(bitmap)
    }

    private fun uploadData() {
        val titleProduct = inputTitle.text.toString()
        val description = inputDescription.text.toString()
        val note = inputNote.text.toString()

        if (validateInput(scroll_root_post, titleProduct, description, note)) {
            val requestTitle = createPartFromString(titleProduct)
            val requestDescription = createPartFromString(description)
            val requestNote = createPartFromString(note)
            val requestLongitude = createPartFromString(OAppUtil.getLongitude()!!)
            val requestLatitude = createPartFromString(OAppUtil.getLatitude()!!)
            val requestType = createPartFromString("image")

            val body: MultipartBody.Part

            if (bitmap != null && !imagePath.isNullOrEmpty()) {
                body = prepareFilePart(bitmap, imagePath)
                presenter.files.add(body)
            }

            presenter.createPost(requestTitle, requestDescription, requestType, requestLatitude, requestLongitude, requestNote)
        }
    }

}
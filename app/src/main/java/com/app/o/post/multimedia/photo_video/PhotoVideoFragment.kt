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
import com.app.o.post.adapter.SelectedImageAdapter
import com.app.o.shared.util.OAppMultimediaUtil
import com.app.o.shared.util.OAppUtil
import kotlinx.android.synthetic.main.fragment_photo_video.*
import okhttp3.RequestBody

class PhotoVideoFragment : OAppFragment(), OAppViewService<CreatedPostResponse> {

    private var index: Int? = -1
    private var isSubmittingItem: Boolean = false

    private var uriValues: ArrayList<String> = arrayListOf()
    private var uriBitmapList: ArrayList<Bitmap> = arrayListOf()

    private lateinit var imagePreview: ImageView
    private lateinit var backgroundImageLayout: RelativeLayout
    private lateinit var buttonPost: Button
    private lateinit var inputTitle: EditText
    private lateinit var inputDescription: EditText
    private lateinit var inputNote: EditText

    private lateinit var presenter: PhotoVideoPresenter
    private lateinit var selectedImageAdapter: SelectedImageAdapter

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
        backgroundImageLayout.setOnClickListener {
            //TODO Check whether if video or image
            if (uriValues.isNullOrEmpty()) {
                openMedia()
            }
        }

        buttonPost = view.findViewById(R.id.button_post) as Button
        buttonPost.setOnClickListener { uploadData() }

        when (index) {
            INDEX_IMAGE -> imagePreview.setBackgroundResource(R.drawable.bg_default_post_image)
            INDEX_VIDEO -> imagePreview.setBackgroundResource(R.drawable.bg_default_post_video)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter = PhotoVideoPresenter(this, mCompositeDisposable)
    }

    override fun showLoading() {
        isSubmittingItem = true
        shouldShowProgress(true)
    }

    override fun hideLoading(statusCode: Int) {
        isSubmittingItem = false
        shouldShowProgress(false)

        if (statusCode == OAppUtil.ON_FINISH_FAILED) {
            showSnackBar(getString(R.string.text_label_error_posting), scroll_root_post)
        }
    }

    override fun onDataResponse(data: CreatedPostResponse) {
        closePage(data.status)
    }

    override fun onSuccessGetImage(values: ArrayList<String>) {
        super.onSuccessGetImage(values)

        imagePreview.visibility = View.GONE

        //TODO Check whether if video or image
        uriValues = values
        uriValues.forEach {
            uriBitmapList.add(BitmapFactory.decodeFile(it))
        }

        selectedImageAdapter = SelectedImageAdapter(fragmentManager!!, uriValues)
        view_pager_selected_images.adapter = selectedImageAdapter
    }

    private fun getRequestType(type: Int): RequestBody? {
        when(type) {
            INDEX_IMAGE -> return createPartFromString(OAppMultimediaUtil.TYPE_IMAGE)
            INDEX_VIDEO -> return createPartFromString(OAppMultimediaUtil.TYPE_VIDEO)
        }

        return null
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
            val requestType = getRequestType(index!!)

            for (i in 0..uriValues.size) {
                val body = prepareFileImagePart(uriBitmapList[i], i, uriValues[i])
                presenter.files.add(body)
            }

            if (!isSubmittingItem) {
                presenter.createPost(requestTitle, requestDescription, requestType!!, requestLatitude, requestLongitude, requestNote)
            }
        }
    }

}
package com.app.o.post.multimedia.photo_video

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

    private lateinit var uriValue: Uri
    private lateinit var imagePreview: ImageView
    private lateinit var backgroundImageLayout: RelativeLayout
    private lateinit var buttonPost: Button
    private lateinit var inputTitle: EditText
    private lateinit var inputDescription: EditText
    private lateinit var inputNote: EditText
    private lateinit var viewPagerImage: ViewPager
    private lateinit var videoFrame: VideoView
    private lateinit var mediaController: MediaController

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
        videoFrame = view.findViewById(R.id.video_frame) as VideoView

        inputTitle = view.findViewById(R.id.input_title_product) as EditText
        inputDescription = view.findViewById(R.id.input_description) as EditText
        inputNote = view.findViewById(R.id.input_note) as EditText

        viewPagerImage = view.findViewById(R.id.view_pager_selected_images) as ViewPager

        backgroundImageLayout = view.findViewById(R.id.view_layout_multimedia) as RelativeLayout
        backgroundImageLayout.setOnClickListener {
            if (index == INDEX_IMAGE) {
                if (uriValues.isNullOrEmpty()) {
                    openMediaImage()
                }
            } else {
                openMediaVideo()
            }
        }

        buttonPost = view.findViewById(R.id.button_post) as Button
        buttonPost.setOnClickListener { uploadData() }

        when (index) {
            INDEX_IMAGE -> imagePreview.setBackgroundResource(R.drawable.bg_default_post_image)
            INDEX_VIDEO -> imagePreview.setBackgroundResource(R.drawable.bg_default_post_video)
        }

        mediaController = MediaController(activity)
        mediaController.setAnchorView(videoFrame)

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
        viewPagerImage.visibility = View.VISIBLE

        try {
            uriValues = values
            uriValues.forEach {
                uriBitmapList.add(OAppMultimediaUtil.getBitmapCorrectOrientation(it))
            }

            selectedImageAdapter = SelectedImageAdapter(fragmentManager!!, uriValues)
            viewPagerImage.adapter = selectedImageAdapter
            selectedImageAdapter.notifyDataSetChanged()
        } catch (exception: Exception) {}
    }

    override fun onSuccessGetVideo(value: Uri) {
        super.onSuccessGetVideo(value)

        imagePreview.visibility = View.GONE
        videoFrame.visibility = View.VISIBLE

        try {
            uriValue = value

            videoFrame.setVideoURI(uriValue)
            videoFrame.setMediaController(mediaController)
            videoFrame.requestFocus()
            videoFrame.start()
        } catch (exception: Exception) {}
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

            if (index == INDEX_IMAGE) {
                if (uriValues.size > 0) {
                    for (i in 0 until uriValues.size) {
                        val body = OAppMultimediaUtil.prepareFileImagePart(
                                "media[" + i.toString() + "]",
                                uriBitmapList[i],
                                uriValues[i])
                        presenter.files.add(body)
                    }
                }
            } else if (index == INDEX_VIDEO) {
                if (!uriValue.path.isNullOrEmpty()) {
                    val body = OAppMultimediaUtil.prepareFileVideoPart(
                            "media[0]",
                            OAppMultimediaUtil.getVideoPath(uriValue, activity!!.parent)!!)
                    presenter.files.add(body)
                }
            }

            if (!isSubmittingItem) {
                presenter.createPost(requestTitle, requestDescription, requestType!!, requestLatitude, requestLongitude, requestNote)
            }
        }
    }

}
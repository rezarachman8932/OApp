package com.app.o.post.multimedia.text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.app.o.R
import com.app.o.api.post.CreatedPostResponse
import com.app.o.base.page.OAppFragment
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppMultimediaUtil
import com.app.o.shared.util.OAppUtil
import kotlinx.android.synthetic.main.fragment_text.*

class TextFragment : OAppFragment(), OAppViewService<CreatedPostResponse> {

    private lateinit var presenter: TextPresenter
    private var isSubmittingItem: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_text, container, false)

        val buttonPost = view.findViewById(R.id.button_post) as Button
        val inputTitle = view.findViewById(R.id.input_title_product) as EditText
        val inputDescription = view.findViewById(R.id.input_description) as EditText
        val inputNote = view.findViewById(R.id.input_note) as EditText

        buttonPost.setOnClickListener {
            val titleProduct = inputTitle.text.toString()
            val description = inputDescription.text.toString()
            val note = inputNote.text.toString()

            if (validateInput(scroll_root_post, titleProduct, description, note)) {
                val requestTitle = createPartFromString(titleProduct)
                val requestDescription = createPartFromString(description)
                val requestNote = createPartFromString(note)
                val requestLongitude = createPartFromString(OAppUtil.getLongitude()!!)
                val requestLatitude = createPartFromString(OAppUtil.getLatitude()!!)
                val requestType = createPartFromString(OAppMultimediaUtil.TYPE_TEXT)

                if (!isSubmittingItem) {
                    presenter.createPost(requestTitle, requestDescription, requestType, requestLatitude, requestLongitude, requestNote)
                }
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter = TextPresenter(this, mCompositeDisposable)
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

}
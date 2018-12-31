package com.app.o.post.text

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.app.o.R
import kotlinx.android.synthetic.main.fragment_text.*

class TextFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_text, container, false)

        val buttonPost = view.findViewById(R.id.button_post) as Button

        val inputTitle = view.findViewById(R.id.input_title_product) as EditText
        val inputDescription = view.findViewById(R.id.input_description) as EditText
        val inputPrice = view.findViewById(R.id.input_price) as EditText

        buttonPost.setOnClickListener {
            val titleProduct = inputTitle.text.toString()
            val description = inputDescription.text.toString()
            val price = inputPrice.text.toString()

            validateInput(titleProduct, description, price)
        }

        return view
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(scroll_root_post, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun validateInput(titleProduct: String, description: String, price: String) {
        if (titleProduct.isEmpty()) {
            showSnackBar(getString(R.string.text_label_title_is_empty))
            return
        }

        if (description.isEmpty()) {
            showSnackBar(getString(R.string.text_label_description_is_empty))
            return
        }

        if (price.isEmpty()) {
            showSnackBar(getString(R.string.text_label_note_is_empty))
            return
        }

        //TODO Go to next page
    }

}
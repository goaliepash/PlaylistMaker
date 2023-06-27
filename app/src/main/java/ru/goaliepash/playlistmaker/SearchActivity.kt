package ru.goaliepash.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var imageViewBack: ImageView
    private lateinit var editTextSearch: EditText

    private var textSearch: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initUI()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoreEditTextSearch(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_STRING, textSearch)
        super.onSaveInstanceState(outState)
    }

    private fun initUI() {
        initImageViewBack()
        initEditTextSearch()
    }

    private fun initImageViewBack() {
        imageViewBack = findViewById(R.id.image_view_back)
        imageViewBack.setOnClickListener { finish() }
    }

    private fun initEditTextSearch() {
        editTextSearch = findViewById(R.id.edit_text_search)
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    setDrawableEndVisibility(false, editTextSearch)
                } else {
                    setDrawableEndVisibility(true, editTextSearch)
                }
                textSearch = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        editTextSearch.addTextChangedListener(searchTextWatcher)
        editTextSearch.onDrawableEndClick {
            textSearch = ""
            editTextSearch.setText(textSearch)
            setDrawableEndVisibility(false, editTextSearch)
            hideKeyboard(editTextSearch)
        }
        setDrawableEndVisibility(false, editTextSearch)
    }

    private fun setDrawableEndVisibility(isVisible: Boolean, editText: EditText) {
        if (isVisible) {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_search,
                0,
                R.drawable.ic_close,
                0
            )
        } else {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
        }
    }

    private fun hideKeyboard(editText: EditText) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    private fun restoreEditTextSearch(savedInstanceState: Bundle) {
        textSearch = savedInstanceState.getString(SEARCH_STRING, "")
        editTextSearch.setText(textSearch)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun EditText.onDrawableEndClick(action: () -> Unit) {
        setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                view as EditText
                val end =
                    if (view.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL) view.left else right
                if (motionEvent.rawX >= (end - view.compoundPaddingEnd)) {
                    action.invoke()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    companion object {
        private const val SEARCH_STRING = "SEARCH_STRING"
    }
}
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initUI()
    }

    private fun initUI() {
        initImageViewBack()
        initEditTextSearch()
    }

    private fun initImageViewBack() {
        val imageViewBack = findViewById<ImageView>(R.id.image_view_back)
        imageViewBack.setOnClickListener { finish() }
    }

    private fun initEditTextSearch() {
        val searchEditText = findViewById<EditText>(R.id.edit_text_search)
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    setDrawableEndVisibility(false, searchEditText)
                } else {
                    setDrawableEndVisibility(true, searchEditText)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        searchEditText.addTextChangedListener(searchTextWatcher)
        searchEditText.onDrawableEndClick {
            searchEditText.setText("")
            setDrawableEndVisibility(false, searchEditText)
            hideKeyboard(searchEditText)
        }
        setDrawableEndVisibility(false, searchEditText)
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

    @SuppressLint("ClickableViewAccessibility")
    fun EditText.onDrawableEndClick(action: () -> Unit) {
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
}
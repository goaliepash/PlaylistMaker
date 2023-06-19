package ru.goaliepash.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initUI()
    }

    private fun initUI() {
        initImageViewBack()
        initImageViewShareApp()
        initImageViewSupport()
        initImageViewTermsOfUse()
    }

    private fun initImageViewBack() {
        val imageViewBack = findViewById<ImageView>(R.id.image_view_back)
        imageViewBack.setOnClickListener { finish() }
    }

    private fun initImageViewShareApp() {
        val imageViewShareApp = findViewById<ImageView>(R.id.image_view_share_app)
        imageViewShareApp.setOnClickListener { imageViewShareAppOnClick() }
    }

    private fun initImageViewSupport() {
        val imageViewSupport = findViewById<ImageView>(R.id.image_view_support)
        imageViewSupport.setOnClickListener { imageViewSupportOnClick() }
    }

    private fun initImageViewTermsOfUse() {
        val imageViewTermsOfUse = findViewById<ImageView>(R.id.image_view_terms_of_use)
        imageViewTermsOfUse.setOnClickListener { imageViewTermsOfUseOnClick() }
    }

    private fun imageViewShareAppOnClick() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.yp_android_course_url))
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    }

    private fun imageViewSupportOnClick() {
        val intent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.student_email)))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.message_to_developers))
        }
        startActivity(intent)
    }

    private fun imageViewTermsOfUseOnClick() {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(getString(R.string.yandex_offer_url))
        }
        startActivity(intent)
    }
}
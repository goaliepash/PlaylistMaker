package ru.goaliepash.playlistmaker.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.ui.App

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
        initSwitchMaterialDarkTheme()
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
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.yp_android_course_url))
            type = "text/plain"
            startActivity(Intent.createChooser(this, null))
        }
    }

    private fun imageViewSupportOnClick() {
        Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.student_email)))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.message_to_developers))
            startActivity(this)
        }
    }

    private fun imageViewTermsOfUseOnClick() {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(getString(R.string.yandex_offer_url))
            startActivity(this)
        }
    }

    private fun initSwitchMaterialDarkTheme() {
        val switchMaterialDarkTheme = findViewById<SwitchMaterial>(R.id.switch_material_dark_theme)
        switchMaterialDarkTheme.apply {
            isChecked = (applicationContext as App).darkTheme
            setOnCheckedChangeListener { _, checked -> (applicationContext as App).switchTheme(checked) }
        }
    }
}
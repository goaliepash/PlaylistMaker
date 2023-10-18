package ru.goaliepash.playlistmaker.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.ActivitySettingsBinding
import ru.goaliepash.playlistmaker.presentation.view_model.SettingsViewModel
import ru.goaliepash.playlistmaker.ui.App

class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel by viewModel<SettingsViewModel>()

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater).also { setContentView(it.root) }
        settingsViewModel.isThemeDark().observe(this) {
            binding.switchMaterialDarkTheme.isChecked = it
        }
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
        binding.imageViewBack.setOnClickListener { finish() }
    }

    private fun initImageViewShareApp() {
        binding.imageViewShareApp.setOnClickListener { imageViewShareAppOnClick() }
    }

    private fun initImageViewSupport() {
        binding.imageViewSupport.setOnClickListener { imageViewSupportOnClick() }
    }

    private fun initImageViewTermsOfUse() {
        binding.imageViewTermsOfUse.setOnClickListener { imageViewTermsOfUseOnClick() }
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
        settingsViewModel.getAppTheme()
        binding.switchMaterialDarkTheme.setOnCheckedChangeListener { _, checked -> (applicationContext as App).switchTheme(checked) }
    }
}
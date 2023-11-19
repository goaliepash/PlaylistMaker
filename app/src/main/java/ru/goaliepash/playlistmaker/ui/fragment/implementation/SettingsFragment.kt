package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.FragmentSettingsBinding
import ru.goaliepash.playlistmaker.presentation.view_model.SettingsViewModel
import ru.goaliepash.playlistmaker.ui.App
import ru.goaliepash.playlistmaker.ui.fragment.BindingFragment

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel.isThemeDark().observe(this) {
            binding.switchMaterialDarkTheme.isChecked = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.imageViewBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initImageViewShareApp() {
        binding.imageViewShareApp.setOnClickListener {
            imageViewShareAppOnClick()
        }
    }

    private fun initImageViewSupport() {
        binding.imageViewSupport.setOnClickListener {
            imageViewSupportOnClick()
        }
    }

    private fun initImageViewTermsOfUse() {
        binding.imageViewTermsOfUse.setOnClickListener {
            imageViewTermsOfUseOnClick()
        }
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
        binding.switchMaterialDarkTheme.setOnCheckedChangeListener { _, checked ->
            (requireActivity().applicationContext as App).switchTheme(checked)
        }
    }
}
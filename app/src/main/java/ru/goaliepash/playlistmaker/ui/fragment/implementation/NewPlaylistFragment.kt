package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.goaliepash.data.utils.Constants
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.FragmentNewPlaylistBinding
import ru.goaliepash.playlistmaker.presentation.view_model.NewPlaylistViewModel
import ru.goaliepash.playlistmaker.ui.fragment.BindingFragment
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : BindingFragment<FragmentNewPlaylistBinding>() {

    private val viewModel by viewModel<NewPlaylistViewModel>()

    private var currentUri: Uri? = null

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentNewPlaylistBinding {
        return FragmentNewPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onStart() {
        super.onStart()
        viewModel.isPlaylistCreated().observe(this) {
            if (it) {
                Toast
                    .makeText(
                        requireContext(),
                        getString(
                            R.string.playlist_created_message,
                            binding.textInputEditTextName.text.toString()
                        ),
                        Toast.LENGTH_SHORT
                    )
                    .show()
                requireActivity().supportFragmentManager.popBackStackImmediate()
            }
        }
    }

    private fun initUI() {
        setupBackButton()
        initImageViewBack()
        initImageViewAddCover()
        initTextInputEditTextName()
        initButtonCreate()
    }

    private fun setupBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackButtonClick()
            }
        })
    }

    private fun initImageViewBack() {
        binding.imageViewBack.setOnClickListener {
            onBackButtonClick()
        }
    }

    private fun initImageViewAddCover() {
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imageViewAddCover.setBackgroundResource(R.drawable.rounded_corners)
                    binding.imageViewAddCover.clipToOutline = true
                    binding.imageViewAddCover.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.imageViewAddCover.setImageURI(uri)
                    currentUri = uri
                }
            }
        binding.imageViewAddCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun initTextInputEditTextName() {
        binding.textInputEditTextName.addTextChangedListener {
            if (it.isNullOrBlank()) {
                disableButtonCreate()
            } else {
                enableButtonCreate()
            }
        }
    }

    private fun initButtonCreate() {
        disableButtonCreate()
        binding.buttonCreate.setOnClickListener {
            val coverUri = if (currentUri != null) saveImageToPrivateStorage() else ""
            val playlist = Playlist(
                name = binding.textInputEditTextName.text.toString(),
                description = binding.textInputEditTextDescription.text.toString(),
                trackIds = listOf(),
                tracksCount = 0,
                coverUri = coverUri,
                dateAdded = System.currentTimeMillis()
            )
            viewModel.onButtonCreateClicked(playlist)
        }
    }

    private fun onBackButtonClick() {
        if (userHasActions()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.playlist_alert_dialog_title))
                .setMessage(getString(R.string.playlist_alert_dialog_message))
                .setNegativeButton(getString(R.string.playlist_alert_dialog_negative)) { _, _ -> }
                .setPositiveButton(getString(R.string.playlist_alert_dialog_positive)) { _, _ ->
                    requireActivity().supportFragmentManager.popBackStackImmediate()
                }
                .show()
        } else {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun userHasActions(): Boolean = currentUri != null
            || !binding.textInputEditTextName.text.isNullOrBlank()
            || !binding.textInputEditTextDescription.text.isNullOrBlank()

    private fun saveImageToPrivateStorage(): String {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            Constants.COVERS_DIR_NAME
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(
            filePath,
            getString(R.string.cover_uri, binding.textInputEditTextName.text.toString())
        )
        val inputStream = currentUri?.let {
            requireActivity().contentResolver.openInputStream(it)
        }
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri().toString()
    }

    private fun enableButtonCreate() {
        binding.buttonCreate.isEnabled = true
        binding.buttonCreate.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.yp_blue)
    }

    private fun disableButtonCreate() {
        binding.buttonCreate.isEnabled = false
        binding.buttonCreate.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.yp_text_gray)
    }
}
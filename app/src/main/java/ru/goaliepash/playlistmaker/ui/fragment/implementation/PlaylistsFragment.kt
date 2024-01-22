package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.FragmentPlaylistsBinding
import ru.goaliepash.playlistmaker.presentation.view_model.PlaylistsViewModel
import ru.goaliepash.playlistmaker.ui.fragment.BindingFragment

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {

    private val viewModel by viewModel<PlaylistsViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initButtonAddNewPlayList()
    }

    private fun initButtonAddNewPlayList() {
        binding.buttonAddNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }
    }

    companion object {

        fun newInstance() = PlaylistsFragment()
    }
}
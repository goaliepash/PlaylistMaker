package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.view.LayoutInflater
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.goaliepash.playlistmaker.databinding.FragmentPlaylistsBinding
import ru.goaliepash.playlistmaker.presentation.view_model.PlaylistsViewModel
import ru.goaliepash.playlistmaker.ui.fragment.BindingFragment

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {

    private val viewModel by viewModel<PlaylistsViewModel>()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    companion object {

        fun newInstance() = PlaylistsFragment()
    }
}
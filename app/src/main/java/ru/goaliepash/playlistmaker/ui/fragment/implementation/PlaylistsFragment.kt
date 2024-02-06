package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.FragmentPlaylistsBinding
import ru.goaliepash.playlistmaker.presentation.state.PlaylistsState
import ru.goaliepash.playlistmaker.presentation.view_model.PlaylistsViewModel
import ru.goaliepash.playlistmaker.ui.adapter.PlaylistAdapter
import ru.goaliepash.playlistmaker.ui.fragment.BindingFragment
import ru.goaliepash.playlistmaker.utils.Constants.CLICK_DEBOUNCE_DELAY
import ru.goaliepash.playlistmaker.utils.debounce

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {

    private val viewModel by viewModel<PlaylistsViewModel>()

    private var playlistAdapter: PlaylistAdapter? = null

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onPlaylistClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                onPlaylistClick(it)
            }
        initUI()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getPlaylists()
        viewModel.getPlaylistsState().observe(viewLifecycleOwner) {
            renderPlaylistsState(it)
        }
    }

    private fun initUI() {
        initButtonAddNewPlayList()
        initRecyclerView()
    }

    private fun initButtonAddNewPlayList() {
        binding.buttonAddNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }
    }

    private fun initRecyclerView() {
        playlistAdapter = PlaylistAdapter(onPlaylistClickDebounce)
        binding.recyclerView.adapter = playlistAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
    }

    private fun renderPlaylistsState(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Loading -> {
                hideRecyclerView()
                hidePlaceHolder()
                showLoader()
            }

            is PlaylistsState.Content -> {
                showRecyclerView()
                hidePlaceHolder()
                hideLoader()
                playlistAdapter?.playlists?.clear()
                playlistAdapter?.playlists?.addAll(state.playlists)
                playlistAdapter?.notifyDataSetChanged()
            }

            is PlaylistsState.Empty -> {
                hideRecyclerView()
                showPlaceholder()
                hideLoader()
            }
        }
    }

    private fun showRecyclerView() {
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun hideRecyclerView() {
        binding.recyclerView.visibility = View.GONE
    }

    private fun showLoader() {
        binding.linearLayoutProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.linearLayoutProgressBar.visibility = View.GONE
    }

    private fun showPlaceholder() {
        binding.linearLayoutNoPlaylists.visibility = View.VISIBLE
    }

    private fun hidePlaceHolder() {
        binding.linearLayoutNoPlaylists.visibility = View.GONE
    }

    private fun onPlaylistClick(playlist: Playlist) {
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_playlistFragment,
            PlaylistFragment.createArgs(playlist.id)
        )
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}
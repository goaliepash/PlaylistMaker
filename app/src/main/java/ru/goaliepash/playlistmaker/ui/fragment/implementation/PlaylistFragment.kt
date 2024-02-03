package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.domain.model.Track
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.FragmentPlaylistBinding
import ru.goaliepash.playlistmaker.presentation.view_model.PlaylistViewModel
import ru.goaliepash.playlistmaker.ui.adapter.TrackAdapter
import ru.goaliepash.playlistmaker.ui.fragment.BindingFragment
import ru.goaliepash.playlistmaker.utils.Constants.CLICK_DEBOUNCE_DELAY
import ru.goaliepash.playlistmaker.utils.Constants.MILLIS_IN_MINUTE
import ru.goaliepash.playlistmaker.utils.debounce

class PlaylistFragment : BindingFragment<FragmentPlaylistBinding>() {

    private val viewModel by viewModel<PlaylistViewModel>()

    private lateinit var playlist: Playlist
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onTrackClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                openTrackActivity(it)
            }

        initUI()
    }

    override fun onStart() {
        super.onStart()
        val playlistId = requireArguments().getInt(ARGS_PLAYLIST_ID)
        viewModel.getPlaylist(playlistId)
        viewModel.getPlaylist().observe(this) {
            renderPlaylist(it)
        }
        viewModel.getPlaylistTracks(playlistId)
        viewModel.getPlaylistTracks().observe(this) {
            renderPlaylistTracks(it)
        }
    }

    private fun initUI() {
        setupBackButton()
        initImageViewBack()
        initRecyclerViewPlaylistTracks()
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

    private fun initRecyclerViewPlaylistTracks() {
        trackAdapter = TrackAdapter(
            onTrackClickListener = onTrackClickDebounce,
            onTrackLongClickListener = {
                onTrackLongClick(it)
            }
        )
        binding.recyclerViewPlaylistTracks.adapter = trackAdapter
        binding.recyclerViewPlaylistTracks.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun onBackButtonClick() {
        requireActivity().supportFragmentManager.popBackStackImmediate()
    }

    private fun openTrackActivity(track: Track) {
        findNavController().navigate(
            R.id.action_playlistFragment_to_audioPlayerFragment,
            AudioPlayerFragment.createArgs(track)
        )
    }

    private fun onTrackLongClick(track: Track): Boolean {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_playlist))
            .setMessage(getString(R.string.delete_playlist_message))
            .setNegativeButton(getString(R.string.delete_track_alert_dialog_negative)) { _, _ -> }
            .setPositiveButton(getString(R.string.delete_track_alert_dialog_positive)) { _, _ ->
                viewModel.deleteTrackFromPlaylist(track.trackId, playlist.id)
            }
            .show()
        return true
    }

    private fun renderPlaylist(currentPlaylist: Playlist) {
        playlist = currentPlaylist
        if (playlist.coverUri.isEmpty()) {
            binding.imageViewCover.setImageResource(R.drawable.ic_cover_track_info_place_holder)
        } else {
            binding.imageViewCover.setImageURI(playlist.coverUri.toUri())
        }
        binding.textViewName.text = playlist.name
        if (playlist.description.isNotEmpty()) {
            binding.textViewDescription.text = playlist.description
        } else {
            binding.textViewDescription.visibility = View.GONE
        }
    }

    private fun renderPlaylistTracks(playlistTracks: List<Track>) {
        initPlaylistProperties(playlistTracks)
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(playlistTracks)
        trackAdapter.notifyDataSetChanged()
    }

    private fun initPlaylistProperties(playlistTracks: List<Track>) {
        val totalTime = playlistTracks.sumOf { track -> track.trackTimeMillis }.toMinutes()
        val trackCounts = playlistTracks.size
        binding.textViewProperties.text = getString(
            R.string.playlist_properties,
            resources.getQuantityString(R.plurals.minutes_plurals, totalTime.toInt(), totalTime),
            resources.getQuantityString(R.plurals.tracks_plurals, trackCounts, trackCounts)
        )
    }

    private fun Long.toMinutes(): Long {
        return this / MILLIS_IN_MINUTE
    }

    companion object {
        private const val ARGS_PLAYLIST_ID = "PLAYLIST_ID"

        fun createArgs(playlistId: Int): Bundle = bundleOf(ARGS_PLAYLIST_ID to playlistId)
    }
}
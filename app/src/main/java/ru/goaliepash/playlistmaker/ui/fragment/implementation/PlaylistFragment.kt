package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment : BindingFragment<FragmentPlaylistBinding>() {

    private val viewModel by viewModel<PlaylistViewModel>()

    private lateinit var playlist: Playlist
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var bottomSheetBehaviorMore: BottomSheetBehavior<ConstraintLayout>

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
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
        initImageViewShare()
        initImageViewMore()
        initRecyclerViewPlaylistTracks()
        initConstraintLayoutMore()
        initTextViewShare()
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

    private fun initImageViewShare() {
        binding.imageViewShare.setOnClickListener {
            onImageViewShareClick()
        }
    }

    private fun initImageViewMore() {
        binding.imageViewMore.setOnClickListener {
            bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.viewOverlay.visibility = View.VISIBLE
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

    private fun initConstraintLayoutMore() {
        bottomSheetBehaviorMore = BottomSheetBehavior.from(binding.constraintLayoutMore).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehaviorMore.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.viewOverlay.visibility = View.GONE
                        }

                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.viewOverlay.visibility = View.VISIBLE
                        }

                        else -> {
                            binding.viewOverlay.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.viewOverlay.alpha = slideOffset
                }
            }
        )
    }

    private fun initTextViewShare() {
        binding.textViewShare.setOnClickListener {
            onImageViewShareClick()
        }
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
            .setTitle(getString(R.string.delete_playlist_track))
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
        initImageViewPlaylistCover()
        initTextViewPlaylistName()
        initTextViewTracksNumber()
    }

    private fun renderPlaylistTracks(playlistTracks: List<Track>) {
        initPlaylistProperties(playlistTracks)
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(playlistTracks)
        trackAdapter.notifyDataSetChanged()
    }

    private fun initImageViewPlaylistCover() {
        with(binding.imageViewPlaylistCover) {
            if (playlist.coverUri.isEmpty()) {
                setImageResource(R.drawable.ic_cover_place_holder)
            } else {
                setBackgroundResource(R.drawable.rounded_corners)
                clipToOutline = true
                setImageURI(playlist.coverUri.toUri())
            }
        }
    }

    private fun initTextViewPlaylistName() {
        binding.textViewPlaylistName.text = playlist.name
    }

    private fun initTextViewTracksNumber() {
        binding.textViewTracksNumber.text = resources.getQuantityString(
            R.plurals.tracks_plurals,
            playlist.tracksCount,
            playlist.tracksCount
        )
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

    private fun onImageViewShareClick() {
        if (playlist.tracksCount != 0) {
            val shareStringBuilder = StringBuilder()
            shareStringBuilder.append(
                resources.getQuantityString(
                    R.plurals.tracks_plurals,
                    playlist.tracksCount,
                    playlist.tracksCount
                )
            )
            trackAdapter.tracks.forEachIndexed { index, track ->
                shareStringBuilder.append(
                    getString(
                        R.string.share_playlist_track,
                        index + 1,
                        track.artistName,
                        track.trackName,
                        formatTrackTime(track.trackTimeMillis)
                    )
                )
            }
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareStringBuilder.toString())
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        } else {
            Toast
                .makeText(
                    requireContext(),
                    getString(R.string.empty_playlist_message),
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    private fun formatTrackTime(timeMillis: Long): String {
        return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(timeMillis)
    }

    private fun Long.toMinutes(): Long {
        return this / MILLIS_IN_MINUTE
    }

    companion object {
        private const val ARGS_PLAYLIST_ID = "PLAYLIST_ID"
        private const val TIME_FORMAT = "mm:ss"

        fun createArgs(playlistId: Int): Bundle = bundleOf(ARGS_PLAYLIST_ID to playlistId)
    }
}
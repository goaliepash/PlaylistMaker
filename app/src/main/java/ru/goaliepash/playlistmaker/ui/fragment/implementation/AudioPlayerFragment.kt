package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.domain.model.Track
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.FragmentAudioPlayerBinding
import ru.goaliepash.playlistmaker.presentation.view_model.AudioPlayerViewModel
import ru.goaliepash.playlistmaker.ui.adapter.PlaylistBottomSheetAdapter
import ru.goaliepash.playlistmaker.ui.fragment.BindingFragment
import ru.goaliepash.playlistmaker.utils.debounce
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerFragment : BindingFragment<FragmentAudioPlayerBinding>() {

    private val viewModel by viewModel<AudioPlayerViewModel>()

    private lateinit var track: Track
    private var playlistBottomSheetAdapter: PlaylistBottomSheetAdapter? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = getTrackFromArguments()
        viewModel.initMediaPlayer(track.previewUrl)
        viewModel.getAudioPlayerState().observe(this) {
            binding.imageButtonPlayPause.isEnabled = it.isPlayButtonEnabled
            binding.imageButtonPlayPause.setImageResource(it.imageResource)
            binding.textViewTime.text = it.progress
        }
        viewModel.getExistsInFavorites().observe(this) {
            binding
                .imageButtonLike
                .setImageResource(
                    if (it) R.drawable.ic_heart_active
                    else R.drawable.ic_heart_inactive
                )
            track.isFavorite = it
        }
        viewModel.getTrackAddedState().observe(this) {
            showToast(it)
        }
        onPlaylistClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, lifecycleScope, false) {
            onPlaylistClick(it)
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAudioPlayerBinding {
        return FragmentAudioPlayerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getPlaylists()
        viewModel.getPlaylistsState().observe(this) {
            playlistBottomSheetAdapter?.playlists?.clear()
            playlistBottomSheetAdapter?.playlists?.addAll(it)
            playlistBottomSheetAdapter?.notifyDataSetChanged()
        }
    }

    private fun initUI() {
        setupBackButton()
        initImageViewBack()
        initImageViewCover()
        initTextViewTrackName()
        initTextViewArtist()
        initTextViewLengthValue()
        initTextViewAlbumValue()
        initTextViewYearValue()
        initTextViewGenreValue()
        initTextViewCountryValue()
        initImageViewAddToTrackList()
        initImageButtonPlayPause()
        initImageButtonLike()
        initConstraintLayoutBottomSheet()
        initButtonAddNewPlaylist()
        initRecyclerViewPlaylists()
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

    private fun initImageViewCover() {
        val artworkUrl100 = track.artworkUrl100
        val artworkUrl512 = artworkUrl100.replaceAfterLast('/', ART_WORK_NEW_VALUE)
        Glide
            .with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.ic_cover_track_info_place_holder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.offset_8)))
            .into(binding.imageViewCover)
    }

    private fun initTextViewTrackName() {
        binding.textViewTrackName.text = track.trackName
    }

    private fun initTextViewArtist() {
        binding.textViewArtist.text = track.artistName
    }

    private fun initTextViewLengthValue() {
        val formattedTrackTime = formatTrackTime(track.trackTimeMillis)
        binding.textViewLengthValue.text = formattedTrackTime
    }

    private fun initTextViewAlbumValue() {
        binding.textViewAlbumValue.text = track.collectionName
    }

    private fun initTextViewYearValue() {
        val formatter = DateTimeFormatter.ofPattern(RELEASED_DATE_FORMAT)
        val releaseDateTime = LocalDateTime.parse(track.releaseDate, formatter)
        binding.textViewYearValue.text = releaseDateTime.year.toString()
    }

    private fun initTextViewGenreValue() {
        binding.textViewGenreValue.text = track.primaryGenreName
    }

    private fun initTextViewCountryValue() {
        binding.textViewCountryValue.text = track.country
    }

    private fun initImageViewAddToTrackList() {
        binding.imageViewAddToTrackList.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.viewOverlay.visibility = View.VISIBLE
        }
    }

    private fun initImageButtonPlayPause() {
        binding.imageButtonPlayPause.setOnClickListener {
            viewModel.onImageButtonPlayPauseClicked()
        }
    }

    private fun initImageButtonLike() {
        binding
            .imageButtonLike
            .setImageResource(
                if (track.isFavorite) R.drawable.ic_heart_active
                else R.drawable.ic_heart_inactive
            )
        binding.imageButtonLike.setOnClickListener {
            viewModel.onImageButtonLikeClicked(track)
        }
    }

    private fun initConstraintLayoutBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.constraintLayoutBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(
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

    private fun initButtonAddNewPlaylist() {
        binding.buttonAddNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
        }
    }

    private fun initRecyclerViewPlaylists() {
        playlistBottomSheetAdapter = PlaylistBottomSheetAdapter(onPlaylistClickDebounce)
        binding.recyclerViewPlaylists.adapter = playlistBottomSheetAdapter
        binding.recyclerViewPlaylists.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getTrackFromArguments(): Track {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(ARGS_TRACK, Track::class.java)!!
        } else {
            requireArguments().getSerializable(ARGS_TRACK) as Track
        }
    }

    private fun showToast(isTrackAdded: Boolean) {
        if (isTrackAdded) {
            Toast.makeText(requireContext(), R.string.track_was_added, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), R.string.track_is_already_exist, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun onPlaylistClick(playlist: Playlist) {
        viewModel.updatePlaylist(track, playlist)
    }

    private fun formatTrackTime(timeMillis: Long): String {
        return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(timeMillis)
    }

    private fun onBackButtonClick() {
        requireActivity().supportFragmentManager.popBackStackImmediate()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val ART_WORK_NEW_VALUE = "512x512bb.jpg"
        private const val TIME_FORMAT = "mm:ss"
        private const val RELEASED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val ARGS_TRACK = "ARGS_TRACK"

        fun createArgs(track: Track): Bundle = bundleOf(ARGS_TRACK to track)
    }
}
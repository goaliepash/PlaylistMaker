package ru.goaliepash.playlistmaker.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.goaliepash.domain.model.Track
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.ActivityAudioPlayerBinding
import ru.goaliepash.playlistmaker.presentation.view_model.AudioPlayerViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private val viewModel by viewModel<AudioPlayerViewModel>()

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        track = getTrackFromIntent()
        viewModel.initMediaPlayer(track.previewUrl)
        viewModel.getAudioPlayerState().observe(this) {
            binding.imageButtonPlayPause.isEnabled = it.isPlayButtonEnabled
            binding.imageButtonPlayPause.setImageResource(it.imageResource)
            binding.textViewTime.text = it.progress
        }
        viewModel.checkIfTrackExistsInFavorites(track.trackId)
        viewModel.getExistState().observe(this) {
            binding.imageButtonLike.setImageResource(if (it) R.drawable.ic_heart_active else R.drawable.ic_heart_inactive)
        }
        initUI()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun getTrackFromIntent(): Track {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_TRACK, Track::class.java)!!
        } else {
            intent.getSerializableExtra(EXTRA_TRACK) as Track
        }
    }

    private fun initUI() {
        initImageViewBack()
        initImageViewCover()
        initTextViewTrackName()
        initTextViewArtist()
        initTextViewLengthValue()
        initTextViewAlbumValue()
        initTextViewYearValue()
        initTextViewGenreValue()
        initTextViewCountryValue()
        initImageButtonPlayPause()
        initImageButtonLike()
    }

    private fun initImageViewBack() {
        binding.imageViewBack.setOnClickListener {
            finish()
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

    private fun initImageButtonPlayPause() {
        binding.imageButtonPlayPause.setOnClickListener {
            viewModel.onImageButtonPlayPauseClicked()
        }
    }

    private fun initImageButtonLike() {
        binding.imageButtonLike.setOnClickListener {
            viewModel.onImageButtonLikeClicked(track)
        }
    }

    private fun formatTrackTime(timeMillis: Long): String {
        return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(timeMillis)
    }

    companion object {
        const val EXTRA_TRACK = "EXTRA_TRACK"

        private const val ART_WORK_NEW_VALUE = "512x512bb.jpg"
        private const val TIME_FORMAT = "mm:ss"
        private const val RELEASED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    }
}
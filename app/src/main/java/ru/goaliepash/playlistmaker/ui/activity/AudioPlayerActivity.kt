package ru.goaliepash.playlistmaker.ui.activity

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.goaliepash.domain.model.Track
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private val mediaPlayer = MediaPlayer()
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            val currentPosition = mediaPlayer.currentPosition.toLong()
            binding.textViewTime.text = formatTrackTime(currentPosition)
            mainThreadHandler.postDelayed(this, UPDATE_TIME_DELAY)
        }
    }

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var track: Track

    private var playerState = STATE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater).also { setContentView(it.root) }
        track = getTrackFromIntent()
        preparePlayer()
        initUI()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacks(updateTimeRunnable)
        mediaPlayer.release()
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
        initTextViewTime()
    }

    private fun initImageViewBack() {
        binding.imageViewBack.setOnClickListener { finish() }
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
        binding.imageButtonPlayPause.setOnClickListener { playbackControl() }
    }

    private fun initTextViewTime() {
        binding.textViewTime.text = formatTrackTime(TRACK_START_TIME)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.imageButtonPlayPause.isClickable = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            mainThreadHandler.removeCallbacks(updateTimeRunnable)
            binding.imageButtonPlayPause.setImageResource(R.drawable.ic_play)
            binding.textViewTime.text = formatTrackTime(TRACK_START_TIME)
            playerState = STATE_PREPARED
        }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun startPlayer() {
        mainThreadHandler.post(updateTimeRunnable)
        mediaPlayer.start()
        binding.imageButtonPlayPause.setImageResource(R.drawable.ic_pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mainThreadHandler.removeCallbacks(updateTimeRunnable)
        mediaPlayer.pause()
        binding.imageButtonPlayPause.setImageResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
    }

    private fun formatTrackTime(timeMillis: Long): String {
        return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(timeMillis)
    }

    companion object {
        const val EXTRA_TRACK = "EXTRA_TRACK"

        private const val ART_WORK_NEW_VALUE = "512x512bb.jpg"
        private const val TIME_FORMAT = "mm:ss"
        private const val RELEASED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 4
        private const val TRACK_START_TIME = 0L
        private const val UPDATE_TIME_DELAY = 500L
    }
}
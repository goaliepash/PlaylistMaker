package ru.goaliepash.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
            textViewTime.text = formatTrackTime(currentPosition)
            mainThreadHandler.postDelayed(this, UPDATE_TIME_DELAY)
        }
    }

    private lateinit var track: Track
    private lateinit var imageViewBack: ImageView
    private lateinit var imageViewCover: ImageView
    private lateinit var textViewTrackName: TextView
    private lateinit var textViewArtist: TextView
    private lateinit var textViewLengthValue: TextView
    private lateinit var textViewAlbumValue: TextView
    private lateinit var textViewYearValue: TextView
    private lateinit var textViewGenreValue: TextView
    private lateinit var textViewCountryValue: TextView
    private lateinit var imageButtonPlayPause: ImageButton
    private lateinit var textViewTime: TextView

    private var playerState = STATE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
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
            intent.getParcelableExtra(EXTRA_TRACK, Track::class.java)!!
        } else {
            intent.getParcelableExtra(EXTRA_TRACK)!!
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
        imageViewBack = findViewById(R.id.image_view_back)
        imageViewBack.setOnClickListener { finish() }
    }

    private fun initImageViewCover() {
        imageViewCover = findViewById(R.id.image_view_cover)
        val artworkUrl100 = track.artworkUrl100
        val artworkUrl512 = artworkUrl100?.replaceAfterLast('/', ART_WORK_NEW_VALUE) ?: ""
        Glide
            .with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.ic_cover_track_info_place_holder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.offset_8)))
            .into(imageViewCover)
    }

    private fun initTextViewTrackName() {
        textViewTrackName = findViewById(R.id.text_view_track_name)
        textViewTrackName.text = track.trackName
    }

    private fun initTextViewArtist() {
        textViewArtist = findViewById(R.id.text_view_artist)
        textViewArtist.text = track.artistName
    }

    private fun initTextViewLengthValue() {
        textViewLengthValue = findViewById(R.id.text_view_length_value)
        val formattedTrackTime = formatTrackTime(track.trackTimeMillis)
        textViewLengthValue.text = formattedTrackTime
    }

    private fun initTextViewAlbumValue() {
        textViewAlbumValue = findViewById(R.id.text_view_album_value)
        textViewAlbumValue.text = track.collectionName.orEmpty()
    }

    private fun initTextViewYearValue() {
        textViewYearValue = findViewById(R.id.text_view_year_value)
        if (track.releaseDate != null) {
            val formatter = DateTimeFormatter.ofPattern(RELEASED_DATE_FORMAT)
            val releaseDateTime = LocalDateTime.parse(track.releaseDate, formatter)
            textViewYearValue.text = releaseDateTime.year.toString()
        }
    }

    private fun initTextViewGenreValue() {
        textViewGenreValue = findViewById(R.id.text_view_genre_value)
        textViewGenreValue.text = track.primaryGenreName.orEmpty()
    }

    private fun initTextViewCountryValue() {
        textViewCountryValue = findViewById(R.id.text_view_country_value)
        textViewCountryValue.text = track.country.orEmpty()
    }

    private fun initImageButtonPlayPause() {
        imageButtonPlayPause = findViewById(R.id.image_button_play_pause)
        imageButtonPlayPause.setOnClickListener { playbackControl() }
    }

    private fun initTextViewTime() {
        textViewTime = findViewById(R.id.text_view_time)
        textViewTime.text = formatTrackTime(TRACK_START_TIME)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            imageButtonPlayPause.isClickable = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            mainThreadHandler.removeCallbacks(updateTimeRunnable)
            imageButtonPlayPause.setImageResource(R.drawable.ic_play)
            textViewTime.text = formatTrackTime(TRACK_START_TIME)
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
        imageButtonPlayPause.setImageResource(R.drawable.ic_pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mainThreadHandler.removeCallbacks(updateTimeRunnable)
        mediaPlayer.pause()
        imageButtonPlayPause.setImageResource(R.drawable.ic_play)
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
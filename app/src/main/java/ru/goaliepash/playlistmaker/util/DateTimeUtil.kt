package ru.goaliepash.playlistmaker.util

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeUtil {

    private const val RELEASED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val TIME_FORMAT = "mm:ss"

    fun getReleasedDateTimeYear(releaseDate: String): String {
        val formatter = DateTimeFormatter.ofPattern(RELEASED_DATE_FORMAT)
        return LocalDateTime.parse(releaseDate, formatter).year.toString()
    }

    fun getFormattedTrackTime(timeMillis: Long): String {
        return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(timeMillis)
    }
}
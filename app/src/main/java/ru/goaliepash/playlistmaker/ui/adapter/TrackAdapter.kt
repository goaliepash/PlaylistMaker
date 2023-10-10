package ru.goaliepash.playlistmaker.ui.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.domain.model.Track
import ru.goaliepash.playlistmaker.ui.listener.OnTrackClickListener
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter(private val onTrackClickListener: OnTrackClickListener) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    val tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { onTrackClickListener.onTrackClick(tracks[position]) }
    }

    override fun getItemCount(): Int = tracks.size

    class TrackViewHolder(itemView: View) : ViewHolder(itemView) {

        private val imageViewCover: ImageView
        private val textViewTrackName: TextView
        private val textViewArtistAndTime: TextView

        init {
            imageViewCover = itemView.findViewById(R.id.image_view_cover)
            textViewTrackName = itemView.findViewById(R.id.text_view_track_name)
            textViewArtistAndTime = itemView.findViewById(R.id.text_view_artist_and_time)
        }

        fun bind(model: Track) {
            Glide
                .with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.ic_cover_place_holder)
                .transform(RoundedCorners(dpToPx(2.0f, itemView.context)))
                .into(imageViewCover)
            textViewTrackName.text = model.trackName
            setTextViewArtistAndTime(model.artistName, model.trackTimeMillis)
        }

        private fun setTextViewArtistAndTime(artistName: String, trackTimeMillis: Long) {
            val formattedTrackTime = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(trackTimeMillis)
            val artistAndTime = itemView.context.getString(R.string.artist_and_time, artistName, formattedTrackTime)
            textViewArtistAndTime.text = artistAndTime
        }

        private fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
        }

        companion object {
            private const val TIME_FORMAT = "mm:ss"
        }
    }
}
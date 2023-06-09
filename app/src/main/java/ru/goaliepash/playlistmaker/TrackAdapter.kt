package ru.goaliepash.playlistmaker

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
import ru.goaliepash.playlistmaker.model.Track

class TrackAdapter(private val trackList: List<Track>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

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
            setTextViewArtistAndTime(model.artistName, model.trackTime)
        }

        private fun setTextViewArtistAndTime(artistName: String, trackTime: String) {
            val artistAndTime = itemView.context.getString(R.string.artist_and_time, artistName, trackTime)
            textViewArtistAndTime.text = artistAndTime
        }

        private fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
        }
    }
}
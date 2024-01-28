package ru.goaliepash.playlistmaker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.ui.listener.OnPlaylistClickListener

class PlaylistBottomSheetAdapter(private val onPlaylistClickListener: OnPlaylistClickListener) :
    RecyclerView.Adapter<PlaylistBottomSheetAdapter.PlaylistBottomSheetViewHolder>() {

    val playlists = mutableListOf<Playlist>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistBottomSheetViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.playlist_bottom_sheet_view, parent, false)
        return PlaylistBottomSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistBottomSheetViewHolder, position: Int) {
        holder.bind(playlist = playlists[position])
        holder.itemView.setOnClickListener {
            onPlaylistClickListener.onPlaylistClick(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size

    class PlaylistBottomSheetViewHolder(itemView: View) : ViewHolder(itemView) {

        private val imageViewCover: ImageView
        private val textViewPlaylistName: TextView
        private val textViewTracksNumber: TextView

        init {
            imageViewCover = itemView.findViewById(R.id.image_view_cover)
            textViewPlaylistName = itemView.findViewById(R.id.text_view_playlist_name)
            textViewTracksNumber = itemView.findViewById(R.id.text_view_tracks_number)
        }

        fun bind(playlist: Playlist) {
            bindImageViewCover(playlist.coverUri)
            textViewPlaylistName.text = playlist.name
            textViewTracksNumber.text = itemView
                .context
                .resources
                .getQuantityString(
                    R.plurals.tracks_plurals,
                    playlist.tracksCount,
                    playlist.tracksCount
                )
        }

        private fun bindImageViewCover(coverUri: String) {
            if (coverUri.isEmpty()) {
                imageViewCover.setImageResource(R.drawable.ic_cover_place_holder)
            } else {
                imageViewCover.setBackgroundResource(R.drawable.rounded_corners)
                imageViewCover.clipToOutline = true
                imageViewCover.setImageURI(coverUri.toUri())
            }
        }
    }
}
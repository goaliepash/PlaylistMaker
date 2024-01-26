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

class PlaylistAdapter : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    val playlists = mutableListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.playlist_view, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size

    class PlaylistViewHolder(itemView: View) : ViewHolder(itemView) {

        private val imageViewCover: ImageView
        private val textViewName: TextView
        private val textViewNumber: TextView

        init {
            imageViewCover = itemView.findViewById(R.id.image_view_cover)
            textViewName = itemView.findViewById(R.id.text_view_name)
            textViewNumber = itemView.findViewById(R.id.text_view_number)
        }

        fun bind(playlist: Playlist) {
            bindImageViewCover(playlist.coverUri)
            textViewName.text = playlist.name
            textViewNumber.text = playlist.tracksCount.toString()
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
package com.example.myspotify.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myspotify.R
import com.example.myspotify.models.Item

class FeaturedPlaylistAdapter(val context: Context, val playlists: List<Item>): RecyclerView.Adapter<FeaturedPlaylistAdapter.PlaylistViewHolder>(){

    class PlaylistViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var playlistImage: ImageView = itemView.findViewById<ImageView>(R.id.featuredImageView)
        var playlistTitle: TextView = itemView.findViewById<TextView>(R.id.featuredTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.featured_list_item,parent, false)
        val viewHolder = PlaylistViewHolder(view)
//        view.setOnClickListener(){
//            listener.onItemClick(playlists[viewHolder.adapterPosition])
//        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.playlistTitle.text = playlist.name
        Glide.with(context).load(playlist.images[0].url).into(holder.playlistImage)

        //val im = playlist.images[0]
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

//    interface PlaylistItemClicked{
//        fun onItemClick(item: Item)
//    }
}
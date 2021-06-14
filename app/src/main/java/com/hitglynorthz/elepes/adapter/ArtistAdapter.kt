package com.hitglynorthz.elepes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.models.Artist

class ArtistAdapter(
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<ArtistAdapter.MyViewHolder>() {

    private var artistList = emptyList<Artist>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val cvArtistRow: MaterialCardView = itemView.findViewById(R.id.cvArtistRow)
        val tvIcon: TextView = itemView.findViewById(R.id.tvIcon)
        val tvArtist: TextView = itemView.findViewById(R.id.tvArtist)
        val tvNalbums: TextView = itemView.findViewById(R.id.tvNalbums)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View) {
            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_artist_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val currentArtist = artistList[position]

        val first = currentArtist.artist?.take(1)
        holder.tvIcon.text = first

        holder.tvArtist.text = currentArtist.artist
        holder.tvNalbums.text = "Albums: ${currentArtist.nAlbums}"
    }

    fun setData(author: List<Artist>) {
        this.artistList = author
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return artistList.size
    }

    interface  OnItemClickListener {
        fun onItemClick(position: Int)
    }

}
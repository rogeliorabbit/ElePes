package com.hitglynorthz.elepes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.card.MaterialCardView
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.models.Library
import com.hitglynorthz.elepes.models.Type

class ArtistAlbumAdapter(
    private val listener: OnItemClickListener
): RecyclerView.Adapter<ArtistAlbumAdapter.MyViewHolder>() {

    private var libraryList = emptyList<Library>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val libraryRow: MaterialCardView = itemView.findViewById(R.id.libraryRow)
        val cvCover: CardView = itemView.findViewById(R.id.cvCover)
        val ivCover: ImageView = itemView.findViewById(R.id.ivCover)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvArtist: TextView = itemView.findViewById(R.id.tvArtist)
        val ivType: ImageView = itemView.findViewById(R.id.ivType)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, cvCover, ivCover)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistAlbumAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_library_grid, parent, false))
    }

    override fun onBindViewHolder(holder: ArtistAlbumAdapter.MyViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val currentItem = libraryList[position]

        holder.tvTitle.text = currentItem.title
        holder.tvArtist.text = currentItem.artist
        when(currentItem.type) {
            Type.CD -> {
                holder.ivType.setImageResource(R.drawable.ic_baseline_music_box)
            }
            Type.VINYL -> {
                holder.ivType.setImageResource(R.drawable.ic_baseline_music_circle)
            }
            Type.CLOUD -> {
                holder.ivType.setImageResource(R.drawable.ic_baseline_cloud_circle)
            }
        }
        Glide.with(context)
            .load(currentItem.img)
            .error(R.drawable.ic_baseline_album)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(holder.ivCover)

        holder.cvCover.transitionName = "cvCoverTransition${currentItem.id}"
        holder.ivCover.transitionName = "ivCoverTransition${currentItem.id}"
        holder.tvTitle.transitionName = "titleTransition${currentItem.id}"
        holder.tvArtist.transitionName = "artistTransition${currentItem.id}"

        /*holder.libraryRow.setOnClickListener {
            //holder.itemView.findNavController().navigate(R.id.action_global_detailsFragment)
        }*/

        /*holder.itemView.setOnLongClickListener {
            requireActivity.startActionMode(this)
            true
        }*/
    }

    override fun getItemCount(): Int {
        return libraryList.size
    }

    fun setData(library: List<Library>) {
        this.libraryList = library
        notifyDataSetChanged()
    }

    interface  OnItemClickListener {
        fun onItemClick(position: Int, cvCover: CardView, ivCover: ImageView)
    }

}
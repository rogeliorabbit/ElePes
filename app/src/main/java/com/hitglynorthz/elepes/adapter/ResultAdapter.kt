package com.hitglynorthz.elepes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.card.MaterialCardView
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.models.search.Cell

class ResultAdapter(private val cell: ArrayList<Cell>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ResultAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val libraryRow: ConstraintLayout = itemView.findViewById(R.id.libraryRow)
        val cvCover: MaterialCardView = itemView.findViewById(R.id.cvCover)
        val ivCover: ImageView = itemView.findViewById(R.id.ivCover)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvArtist: TextView = itemView.findViewById(R.id.tvArtist)
        val ivType: ImageView = itemView.findViewById(R.id.ivType)

        init {
            itemView.setOnClickListener(this)
            cvCover.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_library_grid2, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val currentItem = cell[position]
        holder.tvTitle.text = currentItem.album
        holder.tvArtist.text = currentItem.artist
        holder.ivType.visibility = View.GONE

        Glide.with(context)
            .load(currentItem.img)
            .error(R.drawable.ic_baseline_album)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.ivCover)
    }

    override fun getItemCount(): Int {
        return cell.size
    }

    interface  OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
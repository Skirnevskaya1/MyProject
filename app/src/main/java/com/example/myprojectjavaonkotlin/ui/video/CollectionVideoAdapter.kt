package com.example.myprojectjavaonkotlin.ui.video

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myprojectjavaonkotlin.domain.entity.CollectionEntity
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto

class CollectionVideoAdapter(
    private var data: List<CollectionEntity> = mutableListOf(),
    private var onVideoClickListener: (FavoriteMovieDto) -> Unit = {},
) : RecyclerView.Adapter<CollectionVideoViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(video: List<CollectionEntity>) {
        data = video
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionVideoViewHolder {
        return CollectionVideoViewHolder(
            parent,
            onVideoClickListener
        )
    }

    override fun onBindViewHolder(holder: CollectionVideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int): CollectionEntity = data[position]

    override fun getItemCount(): Int = data.size
}
package com.example.myprojectjavaonkotlin.ui.video

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto

class VideoListAdapter(
    private var data: List<FavoriteMovieDto> = mutableListOf(),
    private var onDetailVideoListener: (FavoriteMovieDto) -> Unit = {},
) : RecyclerView.Adapter<VideoListViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(video: List<FavoriteMovieDto>) {
        data = video
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        return VideoListViewHolder(
            parent,
            onDetailVideoListener
        )
    }

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int): FavoriteMovieDto = data[position]

    override fun getItemCount(): Int = data.size
}
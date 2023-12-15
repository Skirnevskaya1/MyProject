package com.example.myprojectjavaonkotlin.ui.favourites

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto

class FavoriteAdapter(
    private var data: List<FavoriteMovieDto> = mutableListOf(),
    private var onDetailVideoListener: (FavoriteMovieDto) -> Unit = {},
) : RecyclerView.Adapter<FavoriteViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<FavoriteMovieDto>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            parent,
            onDetailVideoListener
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int): FavoriteMovieDto = data[position]

    override fun getItemCount(): Int = data.size

}
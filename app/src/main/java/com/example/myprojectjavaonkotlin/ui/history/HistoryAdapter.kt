package com.example.myprojectjavaonkotlin.ui.history

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto

class HistoryAdapter(
    private var onVideoClickListener: (FavoriteMovieDto) -> Unit = {},
) : RecyclerView.Adapter<HistoryViewHolder>() {

    private var data: List<FavoriteMovieDto> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<FavoriteMovieDto>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            parent,
            onVideoClickListener
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int): FavoriteMovieDto = data[position]

    override fun getItemCount(): Int = data.size
}
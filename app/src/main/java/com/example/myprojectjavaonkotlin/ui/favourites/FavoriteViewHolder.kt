package com.example.myprojectjavaonkotlin.ui.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myprojectjavaonkotlin.R
import com.example.myprojectjavaonkotlin.databinding.ItemVideoListMutualBinding
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.squareup.picasso.Picasso

class FavoriteViewHolder(
    parent: ViewGroup,
    onDetailVideoListener: (FavoriteMovieDto) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_video_list_mutual, parent, false)
) {

    private val binding: ItemVideoListMutualBinding = ItemVideoListMutualBinding.bind(itemView)

    private lateinit var video: FavoriteMovieDto

    fun bind(data: FavoriteMovieDto) {
        this.video = data


        binding.apply {
            nameTextView.text = data.title
            yearReleaseTextView.text = data.yearRelease
            if (data.image.isNotBlank()) {
                //Picasso
                Picasso.get()
                    .load(data.image)
                    .fit() // это значит, что картинка будет размещена по выделенному размеру для нее.
                    .placeholder(R.drawable.uploading_images)
                    .into(coverImageView)
                coverImageView.scaleType =
                    ImageView.ScaleType.FIT_XY// растягиваем картинку на весь элемент
            }
        }

        if (data.isFavorite) {
            binding.favoriteImageView.isVisible = data.isFavorite
        } else {
            R.drawable.favourites_icon_filled
        }
    }

    init {
        itemView.setOnClickListener {
            onDetailVideoListener.invoke(video)
        }
    }
}
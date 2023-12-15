package com.example.myprojectjavaonkotlin.ui.video

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myprojectjavaonkotlin.App
import com.example.myprojectjavaonkotlin.R
import com.example.myprojectjavaonkotlin.databinding.ItemVideoListBinding
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.domain.interactor.LikeInteractor
import com.squareup.picasso.Picasso

class VideoListViewHolder(
    parent: ViewGroup,
    onDetailVideoListener: (FavoriteMovieDto) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_video_list, parent, false)
) {

    private val binding: ItemVideoListBinding = ItemVideoListBinding.bind(itemView)

    private lateinit var video: FavoriteMovieDto
    private val likeInteractor: LikeInteractor by lazy {
        App().di.likeInteractor
    }

    fun bind(favoriteMovieDto: FavoriteMovieDto) {
        this.video = favoriteMovieDto

        binding.nameTextView.text = favoriteMovieDto.title
        binding.yearReleaseTextView.text = favoriteMovieDto.yearRelease
        if (favoriteMovieDto.image.isNotBlank()) {
            //Picasso
            Picasso.get()
                .load(favoriteMovieDto.image)
                .fit() // это значит, что картинка будет размещена по выделенному размеру для нее.
                .placeholder(R.drawable.uploading_images)
                .into(binding.coverImageView)
            binding.coverImageView.scaleType =
                ImageView.ScaleType.FIT_XY// растягиваем картинку на весь элемент
        }
//        binding.favoriteImageView.isVisible = video.isFavorite

        likeInteractor.onLikeChange(
            FavoriteMovieDto(
                id = favoriteMovieDto.id,
                image = favoriteMovieDto.image,
                title = favoriteMovieDto.title,
                description = favoriteMovieDto.description,
                runtimeStr = favoriteMovieDto.runtimeStr,
                genres = favoriteMovieDto.genres,
                genreList = ArrayList(favoriteMovieDto.genreList),
                yearRelease = favoriteMovieDto.yearRelease,
                comment = favoriteMovieDto.comment,
                isFavorite = favoriteMovieDto.isFavorite

            )
        ) { isFavorite ->
            binding.favoriteImageView.isVisible = isFavorite
        }
    }

    init {
        itemView.setOnClickListener {
            onDetailVideoListener.invoke(video)
        }
    }
}
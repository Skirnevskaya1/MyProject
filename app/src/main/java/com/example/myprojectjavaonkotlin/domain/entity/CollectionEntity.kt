package com.example.myprojectjavaonkotlin.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionEntity(
    val genre: GenreDto,
    val movies: MutableList<FavoriteMovieDto> = mutableListOf()
) : Parcelable

package com.example.myprojectjavaonkotlin.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteMovieDto(
    val id: String,
    val image: String,
    val title: String,
    val description: String?,
    val runtimeStr: String?,
    val genres: String,
    val genreList: MutableList<GenreDto>,
    val yearRelease: String? = "1900",
    var comment: String,
    var isFavorite: Boolean = false
) : Parcelable

package com.example.myprojectjavaonkotlin.data

import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.domain.entity.MovieDto

fun MovieDto.mapToFavoriteMovie(
    comment: String = "",
    isFavorite: Boolean = false
): FavoriteMovieDto {
    return FavoriteMovieDto(
        id = this.id,
        image = this.image,
        title = this.title,
        description = this.description,
        runtimeStr = this.runtimeStr,
        genres = this.genres,
        genreList = this.genreList,
        yearRelease = this.yearRelease,
        comment = comment,
        isFavorite = isFavorite
    )
}
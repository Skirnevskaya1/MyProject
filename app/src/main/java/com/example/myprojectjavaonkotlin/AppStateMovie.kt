package com.example.myprojectjavaonkotlin

import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto

sealed class AppStateMovie {
    data class SuccessMovie(val movieData: List<FavoriteMovieDto>) : AppStateMovie()
    data class Error(val error: Throwable) : AppStateMovie()
    object Loading : AppStateMovie()
}

package com.example.myprojectjavaonkotlin.domain.repo

import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto

interface MovieWithFavoriteRepo {

    fun getMovies(genres: List<String>, callback: (List<FavoriteMovieDto>) -> Unit)
    fun getMovie(id: String, callback: (FavoriteMovieDto?) -> Unit)
}
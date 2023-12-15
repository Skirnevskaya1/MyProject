package com.example.myprojectjavaonkotlin.domain.repo

import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto

interface FavoriteMovieRepo {

    fun setFavorite(movieId: String)
    fun setUnFavorite(movieId: String)
    fun setFavorite(movieId: String, isFavorite: Boolean)
    fun getFavorites(): List<String>
    fun isFavorite(movieId: String): Boolean

    fun getFavorite(callback: (List<FavoriteMovieDto>) -> Unit)
}
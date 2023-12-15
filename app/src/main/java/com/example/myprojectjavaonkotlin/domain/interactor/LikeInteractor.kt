package com.example.myprojectjavaonkotlin.domain.interactor

import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto

interface LikeInteractor {

    fun onLikeChange(movieIdEntity: FavoriteMovieDto, callback: (Boolean) -> Unit)
    fun changeLike(movieIdEntity: FavoriteMovieDto)
}
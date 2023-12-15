package com.example.myprojectjavaonkotlin.data.room

import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto

interface HistoryLocalRepo {

    fun getAllHistory(): List<FavoriteMovieDto>
    fun saveEntity(favoriteMovieDto: FavoriteMovieDto)
}
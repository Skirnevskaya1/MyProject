package com.example.myprojectjavaonkotlin.data

import com.example.myprojectjavaonkotlin.domain.repo.GenreRepo

/**
 * список разделов
 */

class GenreRepoImplAlternative : GenreRepo {

    override fun getGenres(): List<String> = mutableListOf(
        "mystery",
        "war",
        "crime",
        "fantasy"
    )
}
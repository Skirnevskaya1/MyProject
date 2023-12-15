package com.example.myprojectjavaonkotlin.data

import com.example.myprojectjavaonkotlin.domain.interactor.AdultInteractor
import com.example.myprojectjavaonkotlin.domain.repo.GenreRepo

/**
 * список разделов
 */

class GenreRepoImpl(
    private val adultInteractor: AdultInteractor
) : GenreRepo {

    override fun getGenres(): List<String> {
        return if (adultInteractor.isAdult.value!!) {
            listOf(
                "mystery",
                "war",
                "crime",
                "fantasy"
            )
        } else {
            listOf(
                "action",
                "comedy",
                "family",
                "history"
            )
        }
    }
}


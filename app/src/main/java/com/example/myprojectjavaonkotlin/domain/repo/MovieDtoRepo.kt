package com.example.myprojectjavaonkotlin.domain.repo

import com.example.myprojectjavaonkotlin.domain.entity.MovieDto

interface MovieDtoRepo {

    fun getMovies(genres: List<String>, callback: (List<MovieDto>) -> Unit)
    fun getMovie(id: String, callback: (MovieDto?) -> Unit)
}
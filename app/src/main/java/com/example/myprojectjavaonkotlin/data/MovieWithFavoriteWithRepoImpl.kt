package com.example.myprojectjavaonkotlin.data

import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.domain.repo.FavoriteMovieRepo
import com.example.myprojectjavaonkotlin.domain.repo.MovieDtoRepo
import com.example.myprojectjavaonkotlin.domain.repo.MovieWithFavoriteRepo

class MovieWithFavoriteWithRepoImpl(
    private val movieDtoRepo: MovieDtoRepo,
    private val favoriteMovieRepo: FavoriteMovieRepo
) : MovieWithFavoriteRepo {

    override fun getMovies(genres: List<String>, callback: (List<FavoriteMovieDto>) -> Unit) {
        movieDtoRepo.getMovies(genres) { movies ->
            val favoriteMovies = movies.map {
                val isFavorite = favoriteMovieRepo.isFavorite(it.id)
                it.mapToFavoriteMovie(isFavorite = isFavorite)
            }
            callback(favoriteMovies)
        }
    }

    override fun getMovie(id: String, callback: (FavoriteMovieDto?) -> Unit) {
        movieDtoRepo.getMovie(id) {
            val isFavorite = favoriteMovieRepo.isFavorite(id)
            val favoriteMovie = it?.mapToFavoriteMovie(isFavorite = isFavorite)
            callback.invoke(favoriteMovie)
        }
    }
}
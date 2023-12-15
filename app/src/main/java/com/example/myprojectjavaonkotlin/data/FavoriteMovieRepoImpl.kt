package com.example.myprojectjavaonkotlin.data

import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.domain.repo.FavoriteMovieRepo

/**
 * MutableSet, HashSet (Set) - это позволяет добавлять только один элемент
 */

class FavoriteMovieRepoImpl : FavoriteMovieRepo {

    private val data: MutableSet<String> = HashSet() //todo save to storage

    override fun setFavorite(movieId: String) {
        data.add(movieId)
    }

    override fun setFavorite(movieId: String, isFavorite: Boolean) {
        if (isFavorite) {
            setFavorite(movieId)
        } else {
            setUnFavorite(movieId)
        }
    }

    override fun setUnFavorite(movieId: String) {
        data.remove(movieId)
    }

    override fun getFavorites(): List<String> {
        return ArrayList(data)
    }

    override fun isFavorite(movieId: String): Boolean {
        return data.contains(movieId)
    }

    override fun getFavorite(callback: (List<FavoriteMovieDto>) -> Unit) {
        TODO("Not yet implemented")
    }
}
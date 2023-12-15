package com.example.myprojectjavaonkotlin.data

import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.domain.interactor.LikeInteractor
import com.example.myprojectjavaonkotlin.domain.repo.FavoriteMovieRepo

class LikeInteractorImpl(
    private val favoriteMovieRepo: FavoriteMovieRepo
) : LikeInteractor {

    // заводим коллекцию
    private val listeners: MutableList<Pair<FavoriteMovieDto, (Boolean) -> Unit>> = mutableListOf()

    override fun onLikeChange(movieIdEntity: FavoriteMovieDto, callback: (Boolean) -> Unit) {
        // здесь делается механизм подписки (актуальные значения)
        val isFavorite = favoriteMovieRepo.isFavorite(movieIdEntity.id)
        callback(isFavorite)

        //кладем в listeners
        listeners.add(Pair(movieIdEntity, callback))
    }

    override fun changeLike(movieIdEntity: FavoriteMovieDto) {
        val isFavorite = favoriteMovieRepo.isFavorite(movieIdEntity.id)
        if (isFavorite) {
            favoriteMovieRepo.setUnFavorite(movieIdEntity.id)
        } else {
            favoriteMovieRepo.setFavorite(movieIdEntity.id)
        }
        //после того как листенер изменился необходимо об этом уведомить
        notifyListeners(movieIdEntity, !isFavorite)
    }

    private fun notifyListeners(movieIdEntity: FavoriteMovieDto, isFavorite: Boolean) {
        //проходимся по всему списку
        listeners.forEach {
            if (it.first == movieIdEntity) {
                it.second.invoke(isFavorite)
            }
        }
    }
}
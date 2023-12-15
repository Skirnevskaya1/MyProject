package com.example.myprojectjavaonkotlin.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.domain.repo.FavoriteMovieRepo
import com.example.myprojectjavaonkotlin.ui.utils.mutable

class FavoriteListViewModel(
    private val favoriteMovieRepo: FavoriteMovieRepo
) : ViewModel() {

    //Сделали класс Factory (Фабрика)
    class Factory(
        private val favoriteMovieRepo: FavoriteMovieRepo,
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteListViewModel(favoriteMovieRepo) as T
        }
    }

    val inProgressLiveData: LiveData<Boolean> = MutableLiveData(false)
    val favoriteListLiveData: LiveData<List<FavoriteMovieDto>> = MutableLiveData()
    val selectedVideoLiveData: LiveData<FavoriteMovieDto> = MutableLiveData()

    init {
        inProgressLiveData.mutable().postValue(true)
        favoriteMovieRepo.getFavorite {
            inProgressLiveData.mutable().postValue(false)
            favoriteListLiveData.mutable().postValue(it)
        }
    }

    fun onVideoClick(favoriteMovieDto: FavoriteMovieDto) {
        selectedVideoLiveData.mutable().postValue(favoriteMovieDto)
    }
}
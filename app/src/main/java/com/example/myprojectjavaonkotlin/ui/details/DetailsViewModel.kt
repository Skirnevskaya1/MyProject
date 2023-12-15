package com.example.myprojectjavaonkotlin.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myprojectjavaonkotlin.data.room.HistoryLocalRepo
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.domain.repo.FavoriteMovieRepo
import com.example.myprojectjavaonkotlin.domain.repo.MovieWithFavoriteRepo
import com.example.myprojectjavaonkotlin.ui.utils.mutable

/**
 * liveData - это такой тип данных который позволяет подписатся на него и все время получать изменения
 * Тмкже liveData умеет кэшировать значения
 *
 * liveData нельзя создать (это абстрактный класс), но у него есть несколько наследников
 * postValue работает с многопоточностью, из любого потока делаем postValue и приходит все на главный поток
 */

class DetailsViewModel(
    private val movieWithFavoriteRepo: MovieWithFavoriteRepo,
    private val favoriteMovieRepo: FavoriteMovieRepo,
    private val videoId: String,
    private val historyLocalRepo: HistoryLocalRepo
) : ViewModel() {

    fun onFavoriteChange(favoriteMovieDto: FavoriteMovieDto) {
        favoriteMovieRepo.setFavorite(favoriteMovieDto.id, !favoriteMovieDto.isFavorite)
    }

    //Сделали класс Factory (Фабрика)
    class Factory(
        private val movieWithFavoriteRepo: MovieWithFavoriteRepo,
        private val favoriteMovieRepo: FavoriteMovieRepo,
        private val videoId: String,
        private val historyLocalRepo: HistoryLocalRepo
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailsViewModel(
                movieWithFavoriteRepo,
                favoriteMovieRepo,
                videoId,
                historyLocalRepo
            ) as T
        }
    }

    // сохранение в БД
    fun saveMovieToDb(favoriteMovieDto: FavoriteMovieDto) {
        historyLocalRepo.saveEntity(favoriteMovieDto)
    }

    val videoLiveData: LiveData<FavoriteMovieDto> = MutableLiveData()

    init {
        //проверяе на наличие данных в videoLiveData.
        // Это необходимо для того чтобы при повороте данные не закачивались заново
        if (videoLiveData.value == null) {
            movieWithFavoriteRepo.getMovie(videoId) {
                it.let {
                    videoLiveData.mutable().postValue(it)
                }
            }
        }
    }
}
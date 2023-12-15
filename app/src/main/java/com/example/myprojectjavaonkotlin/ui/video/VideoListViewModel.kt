package com.example.myprojectjavaonkotlin.ui.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myprojectjavaonkotlin.domain.entity.CollectionEntity
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.domain.interactor.CollectionInteractor
import com.example.myprojectjavaonkotlin.ui.utils.mutable

/**
 * liveData - это такой тип данных который позволяет подписатся на него и все время получать изменения
 * Тмкже liveData умеет кэшировать значения
 *
 * liveData нельзя создать (это абстрактный класс), но у него есть несколько наследников
 * postValue работает с многопоточностью, из любого потока делаем postValue и приходит все на главный поток
 */

class VideoListViewModel(
    private val collectionVideoRepo: CollectionInteractor
) : ViewModel() {

    val inProgressLiveData: LiveData<Boolean> = MutableLiveData(false)
    val videoListLiveData: LiveData<List<CollectionEntity>> = MutableLiveData()
    val selectedVideoLiveData: LiveData<FavoriteMovieDto> = MutableLiveData()

    init {
        if (videoListLiveData.value == null) {
            inProgressLiveData.mutable().postValue(true)
            collectionVideoRepo.getCollections {
                inProgressLiveData.mutable().postValue(false)
                videoListLiveData.mutable().postValue(it)
            }
        }
    }

    fun onVideoClick(favoriteMovieDto: FavoriteMovieDto) {
        selectedVideoLiveData.mutable().postValue(favoriteMovieDto)
    }
}
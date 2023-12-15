package com.example.myprojectjavaonkotlin.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myprojectjavaonkotlin.App.Companion.getHistoryMovieViewingDao
import com.example.myprojectjavaonkotlin.AppStateMovie
import com.example.myprojectjavaonkotlin.data.room.HistoryLocalRepo
import com.example.myprojectjavaonkotlin.data.room.HistoryLocalRepoImpl
import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto
import com.example.myprojectjavaonkotlin.ui.utils.mutable

class HistoryViewModel(
    private val historyLocalRepo: HistoryLocalRepo = HistoryLocalRepoImpl(getHistoryMovieViewingDao())
) : ViewModel() {

    val historyLiveData: LiveData<AppStateMovie> = MutableLiveData()

    fun getAllHistory() {
        historyLiveData.mutable().value = AppStateMovie.Loading
        val history = historyLocalRepo.getAllHistory()
        historyLiveData.mutable().value = AppStateMovie.SuccessMovie(history)
    }

    val selectedVideoLiveData: LiveData<FavoriteMovieDto> = MutableLiveData()

    fun onVideoClick(favoriteMovieDto: FavoriteMovieDto) {
        selectedVideoLiveData.mutable().postValue(favoriteMovieDto)
    }
}
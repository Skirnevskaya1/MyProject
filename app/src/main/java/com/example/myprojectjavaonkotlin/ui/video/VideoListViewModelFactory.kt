package com.example.myprojectjavaonkotlin.ui.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myprojectjavaonkotlin.domain.interactor.CollectionInteractor

class VideoListViewModelFactory(private val collectionVideoRepo: CollectionInteractor) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoListViewModel(collectionVideoRepo) as T
    }
}
package com.example.myprojectjavaonkotlin.domain.interactor

import androidx.lifecycle.MutableLiveData

interface AdultInteractor {
    val isAdult: MutableLiveData<Boolean>

}
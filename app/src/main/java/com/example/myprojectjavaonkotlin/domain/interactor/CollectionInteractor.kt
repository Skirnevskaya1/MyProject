package com.example.myprojectjavaonkotlin.domain.interactor

import com.example.myprojectjavaonkotlin.domain.entity.CollectionEntity

interface CollectionInteractor {

    fun getCollections(callback: (List<CollectionEntity>) -> Unit)
}
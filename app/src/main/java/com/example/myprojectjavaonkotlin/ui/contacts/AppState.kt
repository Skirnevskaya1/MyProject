package com.example.myprojectjavaonkotlin.ui.contacts

sealed class AppState {

    data class Success(val data: List<String>) : AppState()
    object Loading : AppState()
}

package com.example.myprojectjavaonkotlin.ui.utils

import android.content.Context
import com.example.myprojectjavaonkotlin.App.Companion.getHistoryMovieViewingDao
import com.example.myprojectjavaonkotlin.BuildConfig
import com.example.myprojectjavaonkotlin.MyReceiver
import com.example.myprojectjavaonkotlin.data.*
import com.example.myprojectjavaonkotlin.data.retrofit.ImdbApi
import com.example.myprojectjavaonkotlin.data.retrofit.RetrofitMovieDtoRepoImpl
import com.example.myprojectjavaonkotlin.data.room.HistoryLocalRepo
import com.example.myprojectjavaonkotlin.data.room.HistoryLocalRepoImpl
import com.example.myprojectjavaonkotlin.domain.interactor.AdultInteractor
import com.example.myprojectjavaonkotlin.domain.interactor.CollectionInteractor
import com.example.myprojectjavaonkotlin.domain.interactor.LikeInteractor
import com.example.myprojectjavaonkotlin.domain.repo.FavoriteMovieRepo
import com.example.myprojectjavaonkotlin.domain.repo.GenreRepo
import com.example.myprojectjavaonkotlin.domain.repo.MovieDtoRepo
import com.example.myprojectjavaonkotlin.domain.repo.MovieWithFavoriteRepo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://imdb-api.com/ru/API/"

class Di(
    private val context: Context
) {

    val myReceiver: MyReceiver by lazy { MyReceiver() }

    // todo переделать
    private val genreRepo: GenreRepo by lazy {
        GenreRepoImpl(adultInteractor)
    }

    val adultInteractor: AdultInteractor by lazy {
        SharedPrefAdultInteractionImpl(context)
    }

    val historyLocalRepo: HistoryLocalRepo by lazy {
        HistoryLocalRepoImpl(getHistoryMovieViewingDao()) //реализация в App
    }

    val likeInteractor: LikeInteractor by lazy {
        LikeInteractorImpl(favoriteMovieRepo)
    }

    val collectionInteractor: CollectionInteractor by lazy {
        CollectionInteractorImpl(genreRepo, movieWithFavoriteRepo)
    }

    private val movieDtoRepo: MovieDtoRepo by lazy {
        RetrofitMovieDtoRepoImpl(imdbApi, BuildConfig.apiKey, context)
    }

    val favoriteMovieRepo: FavoriteMovieRepo = FavoriteMovieRepoImpl()

    val movieWithFavoriteRepo: MovieWithFavoriteRepo by lazy {
        MovieWithFavoriteWithRepoImpl(movieDtoRepo, favoriteMovieRepo)
    }
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val imdbApi: ImdbApi by lazy { retrofit.create(ImdbApi::class.java) }
}
package com.example.myprojectjavaonkotlin.data.retrofit

import com.example.myprojectjavaonkotlin.domain.entity.MovieDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImdbApi {

    @GET("AdvancedSearch/{apiKey}/")
    fun loadMovies(
        @Path("apiKey") apiKey: String,
        @Query("genres") genres: List<String>
    ): Call<AdvancedSearchResponseDto>

    @GET("Title/{apiKey}/{id}/")
    fun loadMovie(
        @Path("apiKey") apiKey: String,
        @Path("id") id: String
    ): Call<MovieDto>
}

data class AdvancedSearchResponseDto(
    val queryString: String,
    val results: MutableList<MovieDto>
)
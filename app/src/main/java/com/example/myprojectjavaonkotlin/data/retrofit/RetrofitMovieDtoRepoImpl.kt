package com.example.myprojectjavaonkotlin.data.retrofit

import android.content.Context
import android.widget.Toast
import com.example.myprojectjavaonkotlin.domain.entity.MovieDto
import com.example.myprojectjavaonkotlin.domain.repo.MovieDtoRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitMovieDtoRepoImpl(
    private val imdbApi: ImdbApi,
    private val apiKey: String,
    private val context: Context
) : MovieDtoRepo {

    override fun getMovies(genres: List<String>, callback: (List<MovieDto>) -> Unit) {

        imdbApi.loadMovies(apiKey, genres).enqueue(object : Callback<AdvancedSearchResponseDto> {
            override fun onResponse(
                call: Call<AdvancedSearchResponseDto>,
                response: Response<AdvancedSearchResponseDto>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    callback(body.results)
                    Toast.makeText(context, "Size: " + body.results.size, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Error code" + response.code(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<AdvancedSearchResponseDto>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun getMovie(id: String, callback: (MovieDto?) -> Unit) {

        imdbApi.loadMovie(apiKey, id).enqueue(object : Callback<MovieDto> {
            override fun onResponse(call: Call<MovieDto>, response: Response<MovieDto>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    callback(body)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<MovieDto>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}

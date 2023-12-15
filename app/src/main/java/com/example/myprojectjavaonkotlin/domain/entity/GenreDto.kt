package com.example.myprojectjavaonkotlin.domain.entity


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreDto(
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    val genre: String
) : Parcelable
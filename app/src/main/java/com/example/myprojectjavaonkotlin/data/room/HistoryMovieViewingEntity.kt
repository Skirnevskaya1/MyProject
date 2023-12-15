package com.example.myprojectjavaonkotlin.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historyMovieViewing")
class HistoryMovieViewingEntity(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "yearRelease")
    val yearRelease: String?,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)


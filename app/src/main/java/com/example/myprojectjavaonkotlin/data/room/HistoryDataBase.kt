package com.example.myprojectjavaonkotlin.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * version = 1 - Обязательно должна быть версия, она для того чтобы провести меграцию БД из одной БД
 * в обновленную БД (при меграции делается специальный клас меграции)
 *
 */

@Database(entities = [HistoryMovieViewingEntity::class], version = 1, exportSchema = false)
abstract class HistoryDataBase : RoomDatabase() {
    abstract fun historyMovieViewingDao(): HistoryMovieViewingDao
}
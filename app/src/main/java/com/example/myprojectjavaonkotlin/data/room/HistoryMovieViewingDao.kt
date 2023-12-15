package com.example.myprojectjavaonkotlin.data.room

import androidx.room.*

@Dao
interface HistoryMovieViewingDao {

    // получаем все записи
    @Query("SELECT * FROM historyMovieViewing")
    fun getAll(): List<HistoryMovieViewingEntity>

    // для фильтрации. чтобы список не пополнялся одинаковыми наименованиями
    @Query("SELECT * FROM historyMovieViewing WHERE title LIKE :title")
    fun getDataBy(title: String): List<HistoryMovieViewingEntity>

    // Если будет поподатся такаяже сущьностиь то она будет заменять существующую или можно настроить - будет игнорировать
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(historyMovieViewingEntity: HistoryMovieViewingEntity)

    @Update
    fun update(historyMovieViewingEntity: HistoryMovieViewingEntity)

    @Delete
    fun delete(historyMovieViewingEntity: HistoryMovieViewingEntity)
}
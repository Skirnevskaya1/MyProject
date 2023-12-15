package com.example.myprojectjavaonkotlin

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.room.Room
import com.example.myprojectjavaonkotlin.data.room.HistoryDataBase
import com.example.myprojectjavaonkotlin.data.room.HistoryMovieViewingDao
import com.example.myprojectjavaonkotlin.ui.utils.Di

/**
 * Здесь создаем репозиторий. Репо должна быть одна, а не создаватся каждый раз в каждом фрагменте.
 * этот класс для того чтобы воспользоватся application.
 * Необходимо прописать в манифесте данный класс
 * android:name=".App"
 * Any - это базовый объект, это тип для всего. Map это ключ - значение
 */

class App : Application() {

    val di: Di by lazy { Di(this) }

    override fun onCreate() {
        super.onCreate()
        // для контактов
        context = applicationContext

        appInstance = this

        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(di.myReceiver, it)
        }

        registerReceiver(
            di.myReceiver, IntentFilter(
                Intent.ACTION_BATTERY_LOW
            )
        )
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        private var appInstance: App? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"

        /**
         * двойное сравнение if (db == null) - это патерн дабл чек. Он применяется когда вкл. synchronized
         * synchronized - накладывает большие ограничения. В нем синхранизируются потоки если туда много
         * потоков будут обращатся.
         * Двойное сравнение необходимо для того чтобы когда зашли (после первой проверки) в synchronized то
         * при выходе из synchronized db уже может быть не null
         */
        fun getHistoryMovieViewingDao(): HistoryMovieViewingDao {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null)
                            throw IllegalStateException("Application is null DB")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            HistoryDataBase::class.java,
                            DB_NAME
                        )
                            .allowMainThreadQueries()//переделать, должно быть не в основном потоке
                            .build()
                    }
                }
            }
            return db!!.historyMovieViewingDao()
        }
    }
}

interface IContextProvider {
    val context: Context
}

object ContextProvider : IContextProvider {

    override val context: Context
        get() = App.context

}
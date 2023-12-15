package com.example.myprojectjavaonkotlin.data

import com.example.myprojectjavaonkotlin.domain.entity.CollectionEntity
import com.example.myprojectjavaonkotlin.domain.interactor.CollectionInteractor
import com.example.myprojectjavaonkotlin.domain.repo.GenreRepo
import com.example.myprojectjavaonkotlin.domain.repo.MovieWithFavoriteRepo

/**
 * genreRepo - С помощью этого достаем разделы (пойдет по вертикале)
 * movieDtoRepo - С помощью этого достаем фильмы (распределим по горизонтали, если в фильми
 * присудствует соответствующая категория фильми (онже раздел) значит этот фильм пойдет
 * в соответствующий раздел.
 */

class CollectionInteractorImpl(
    private val genreRepo: GenreRepo,
    private val movieWithFavoriteRepo: MovieWithFavoriteRepo
) : CollectionInteractor {

    //получаем коллекцию фильмов
    override fun getCollections(callback: (List<CollectionEntity>) -> Unit) {
        val genres = genreRepo.getGenres()
        // что-то вроде таблицы для облегчения поиска (String - жанр, CollectionEntity - пришедшие значения)
        val hashMapMovies = HashMap<String, CollectionEntity>()

        //скачиваем список фильмов
        movieWithFavoriteRepo.getMovies(genres) { movies ->
            //проходим по фильмам
            movies.forEach { film ->
                //проходим по всем жанрам
                film.genreList.forEach { genre ->
                    // Кладем жанры в collection
                    val collection = hashMapMovies[genre.genre]
                        //если collection не равен нул, кладем в жанр (раздел) фильм
                        ?.apply {
                            this.movies.add(film)
                            //Если равен нул, создаем новый жанр (раздел)
                        } ?: CollectionEntity(genre, mutableListOf(film))
                    //получаем коллекцию и кладем ее обратно
                    hashMapMovies[genre.genre] = collection
                }
            }
            callback(hashMapMovies.values.toList())//превратили в hashMapMovies отдали в callback
        }
    }
}
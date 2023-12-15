package com.example.myprojectjavaonkotlin.data.room

import com.example.myprojectjavaonkotlin.domain.entity.FavoriteMovieDto

class HistoryLocalRepoImpl(
    private val historyMovieViewingDao: HistoryMovieViewingDao
) : HistoryLocalRepo {

    override fun getAllHistory(): List<FavoriteMovieDto> {
        return mapFavoriteMovieDtoToHistoryEntity(historyMovieViewingDao.getAll())
    }

    override fun saveEntity(favoriteMovieDto: FavoriteMovieDto) {
        historyMovieViewingDao.insert(mapHistoryEntityToEntity(favoriteMovieDto))
    }

    private fun mapFavoriteMovieDtoToHistoryEntity(entityList: List<HistoryMovieViewingEntity>): List<FavoriteMovieDto> {

        return entityList.map {
            FavoriteMovieDto(
                title = it.title,
                image = it.image,
                yearRelease = it.yearRelease,
                isFavorite = it.isFavorite,

                id = it.id,
                description = "Обычный фильм",
                runtimeStr = "",
                genres = "comedy",
                genreList = ArrayList(),
                comment = "Интересный"
            )
        }
    }

    private fun mapHistoryEntityToEntity(favoriteMovieDto: FavoriteMovieDto): HistoryMovieViewingEntity {
        return HistoryMovieViewingEntity(
            favoriteMovieDto.id,
            favoriteMovieDto.image,
            favoriteMovieDto.title,
            favoriteMovieDto.yearRelease,
            favoriteMovieDto.isFavorite
        )
    }
}
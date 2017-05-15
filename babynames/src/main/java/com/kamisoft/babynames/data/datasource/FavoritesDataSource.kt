package com.kamisoft.babynames.data.datasource

import com.kamisoft.babynames.domain.model.Favorite

interface FavoritesDataSource {

    fun isFavorite(parent:String, gender: NamesDataSource.Gender, name: String): Boolean

    fun saveFavorite(favorite: Favorite): Long

    fun deleteFavorite(favorite: Favorite)

    fun getFavorites(parent: String, gender: NamesDataSource.Gender): List<String>
}
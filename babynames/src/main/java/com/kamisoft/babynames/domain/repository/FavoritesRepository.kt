package com.kamisoft.babynames.domain.repository

import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.model.Favorite

interface FavoritesRepository {

    fun isFavorite(parent: String, gender: NamesDataSource.Gender, name: String): Boolean

    fun saveOrRemoveFavoriteName(favorite: Favorite)

    fun getFavorites(parent: String, gender: NamesDataSource.Gender): List<String>
}
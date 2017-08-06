package com.kamisoft.babynames.data.datasource

import com.kamisoft.babynames.domain.model.Favorite
import com.kamisoft.babynames.domain.model.Gender

interface FavoritesDataSource {

    fun isFavorite(parent: String, gender: Gender, name: String): Boolean

    fun saveFavorite(favorite: Favorite): Long

    fun deleteFavorite(favorite: Favorite)

    fun getFavorites(parent: String, gender: Gender): List<String>
}
package com.kamisoft.babynames.data.datasource

import com.kamisoft.babynames.domain.model.Favorite

interface FavoritesDataSource {

    fun existFavorite(favorite: Favorite): Boolean

    fun saveFavorite(favorite: Favorite): Long

    fun deleteFavorite(favorite: Favorite)
}
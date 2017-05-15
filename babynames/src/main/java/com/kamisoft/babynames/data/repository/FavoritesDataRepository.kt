package com.kamisoft.babynames.data.repository

import com.kamisoft.babynames.data.datasource.FavoritesDataFactory
import com.kamisoft.babynames.domain.model.Favorite
import com.kamisoft.babynames.domain.repository.FavoritesRepository

class FavoritesDataRepository(val favoritesDataFactory: FavoritesDataFactory) : FavoritesRepository {

    override fun saveOrRemoveFavoriteName(favorite: Favorite) {
        val favoritesDataSource = favoritesDataFactory.create()

        if (favoritesDataSource.existFavorite(favorite)) {
            favoritesDataSource.deleteFavorite(favorite)
        } else {
            favoritesDataSource.saveFavorite(favorite)
        }
    }

}
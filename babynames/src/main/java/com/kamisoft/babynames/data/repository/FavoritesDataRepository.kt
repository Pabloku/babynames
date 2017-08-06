package com.kamisoft.babynames.data.repository

import com.kamisoft.babynames.data.datasource.FavoritesDataFactory
import com.kamisoft.babynames.domain.model.Favorite
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.domain.repository.FavoritesRepository

class FavoritesDataRepository(val favoritesDataFactory: FavoritesDataFactory) : FavoritesRepository {

    override fun isFavorite(parent: String, gender: Gender, name: String): Boolean {
        val favoritesDataSource = favoritesDataFactory.create()
        return favoritesDataSource.isFavorite(parent, gender, name)
    }

    override fun saveOrRemoveFavoriteName(favorite: Favorite) {
        val favoritesDataSource = favoritesDataFactory.create()

        if (favoritesDataSource.isFavorite(favorite.parent, Gender.values()[favorite.gender], favorite.babyName)) {
            favoritesDataSource.deleteFavorite(favorite)
        } else {
            favoritesDataSource.saveFavorite(favorite)
        }
    }

    override fun getFavorites(parent: String, gender: Gender): List<String> {
        val favoritesDataSource = favoritesDataFactory.create()
        return favoritesDataSource.getFavorites(parent, gender)
    }


}
package com.kamisoft.babynames.domain.usecase

import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.model.Favorite
import com.kamisoft.babynames.domain.repository.FavoritesRepository

class SaveFavoriteName(val favoriteRepository: FavoritesRepository) {

    fun saveFavoriteName(parent: String, gender: NamesDataSource.Gender, name: String) {
        val favorite = Favorite(parent, gender = gender.ordinal, babyName = name)
        favoriteRepository.saveOrRemoveFavoriteName(favorite)
    }
}
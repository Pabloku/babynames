package com.kamisoft.babynames.domain.usecase

import com.kamisoft.babynames.domain.model.Favorite
import com.kamisoft.babynames.domain.repository.FavoritesRepository

class SaveFavoriteName(val favoriteRepository: FavoritesRepository) {

    fun saveFavoriteName(name: String) {
        val favorite = Favorite(parent = "Father1", gender = 1, babyName = name)
        favoriteRepository.saveOrRemoveFavoriteName(favorite)
    }
}
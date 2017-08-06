package com.kamisoft.babynames.domain.usecase

import com.kamisoft.babynames.domain.model.Favorite
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.domain.repository.FavoritesRepository

class SaveFavoriteName(val favoriteRepository: FavoritesRepository) {

    fun saveFavoriteName(parent: String, gender: Gender, name: String) {
        val favorite = Favorite(parent, gender = gender.ordinal, babyName = name)
        favoriteRepository.saveOrRemoveFavoriteName(favorite)
    }
}
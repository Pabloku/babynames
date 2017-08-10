package com.kamisoft.babynames.domain.usecase

import com.kamisoft.babynames.domain.model.Favorite
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.domain.repository.FavoritesRepository
import com.kamisoft.babynames.domain.repository.NamesRepository

class SaveFavoriteName(val favoriteRepository: FavoritesRepository, val namesRepository: NamesRepository) {

    fun saveFavoriteName(parent: String, gender: Gender, name: String) {
        val favorite = Favorite(parent, gender = gender.ordinal, babyName = name)
        favoriteRepository.saveOrRemoveFavoriteName(favorite)
    }

    fun increaseNameLikedCounter(gender: Gender, name: String){
        namesRepository.increaseNameLikedCounter(gender, name)
    }

    fun decreaseNameLikedCounter(gender: Gender, name: String){
        namesRepository.decreaseNameLikedCounter(gender, name)
    }
}
package com.kamisoft.babynames.domain.repository

import com.kamisoft.babynames.domain.model.Favorite

interface FavoritesRepository {
    fun saveOrRemoveFavoriteName(favorite: Favorite)
}
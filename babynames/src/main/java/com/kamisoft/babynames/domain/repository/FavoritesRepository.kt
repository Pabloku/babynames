package com.kamisoft.babynames.domain.repository

import com.kamisoft.babynames.domain.model.Favorite
import com.kamisoft.babynames.domain.model.Gender

interface FavoritesRepository {

    fun isFavorite(parent: String, gender: Gender, name: String): Boolean

    fun saveOrRemoveFavoriteName(favorite: Favorite)

    fun getFavorites(parent: String, gender: Gender): List<String>
}
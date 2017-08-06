package com.kamisoft.babynames.domain.usecase

import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.domain.repository.FavoritesRepository
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class GetFavoriteList(val favoritesRepository: FavoritesRepository) {

    fun getFavorites(parent: String, gender: Gender, callback: (List<String>) -> Unit) {
        doAsync {
            val favoriteList = loadNames(parent, gender)
            uiThread { callback.invoke(favoriteList) }
        }
    }

    private fun loadNames(parent: String, gender: Gender): List<String> {
        return favoritesRepository.getFavorites(parent, gender)
    }

}
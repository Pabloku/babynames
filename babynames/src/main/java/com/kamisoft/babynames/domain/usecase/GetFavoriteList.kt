package com.kamisoft.babynames.domain.usecase

import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.repository.FavoritesRepository
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class GetFavoriteList(val favoritesRepository: FavoritesRepository) {

    fun getFavorites(parent: String, gender: NamesDataSource.Gender, callback: (List<String>) -> Unit) {
        doAsync {
            val favoriteList = loadNames(parent, gender)
            uiThread { callback.invoke(favoriteList) }
        }
    }

    private fun loadNames(parent: String, gender: NamesDataSource.Gender): List<String> {
        return favoritesRepository.getFavorites(parent, gender)
    }

}
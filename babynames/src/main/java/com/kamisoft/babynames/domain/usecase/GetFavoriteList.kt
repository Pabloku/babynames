package com.kamisoft.babynames.domain.usecase

import android.os.Handler
import android.os.Looper
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.repository.FavoritesRepository

class GetFavoriteList(val favoritesRepository: FavoritesRepository) {

    class CallBacks {
        interface FavoritesCallback {
            fun favoritesLoaded(favoriteList: List<String>)
        }
    }

    fun getFavorites(parent: String, gender: NamesDataSource.Gender, callback: CallBacks.FavoritesCallback) {
        Thread(GetFavorites(parent, gender, callback)).start()
    }

    private fun loadNames(parent: String, gender: NamesDataSource.Gender, callback: CallBacks.FavoritesCallback) {
        val favoriteList = favoritesRepository.getFavorites(parent, gender)
        Handler(Looper.getMainLooper()).post(FavoritesLoaded(callback, favoriteList))
    }

    private inner class GetFavorites(val parent: String, val gender: NamesDataSource.Gender, val callback: CallBacks.FavoritesCallback) : Runnable {
        override fun run() {
            loadNames(parent, gender, callback)
        }
    }

    private inner class FavoritesLoaded(val callback: CallBacks.FavoritesCallback, val names: List<String>) : Runnable {
        override fun run() {
            callback.favoritesLoaded(names)
        }
    }

}
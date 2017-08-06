package com.kamisoft.babynames.presentation.choose_name

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.usecase.GetFavoriteList
import com.kamisoft.babynames.domain.usecase.SaveFavoriteName
import com.kamisoft.babynames.logger.Logger
import com.kamisoft.babynames.presentation.model.BabyNameLikable
import com.rahulrav.futures.Future
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

class ChooseNamePresenter(
        private val getFavoritesUseCase: GetFavoriteList,
        private val saveFavoriteUseCase: SaveFavoriteName) :
        MvpBasePresenter<ChooseNameView>() {

    private var favoriteCounter = 0

    fun start(listFuture: Future<List<BabyName>>) {
        val pullToRefresh = true
        view?.initViews()
        view?.showLoading(!pullToRefresh)
        loadData(listFuture)
    }

    private fun loadData(listFuture: Future<List<BabyName>>) {
        Logger.debug("Future: loadData")
        launch(CommonPool) {
            Logger.debug("Future: loadData launch")
            if (listFuture.ready) {
                Logger.debug("Future: is ready")
                val babyNames = listFuture.result?.toList() ?: emptyList()
                view?.setData(parseToBabyNamesLikable(babyNames))
                view?.showContent()
            } else {
                Logger.debug("Future: not ready")
                val babyNames = listFuture.await(30000)?.toList() ?: emptyList()//TODO Ese 30000...
                view?.setData(parseToBabyNamesLikable(babyNames))
                view?.showContent()
            }
        }
    }

    private fun parseToBabyNamesLikable(babyNames: List<BabyName>): List<BabyNameLikable> {
        return babyNames.map { BabyNameLikable(it.name, it.origin, it.meaning, liked = false) }
    }

    fun loadFavorites(parent: String?, gender: NamesDataSource.Gender) {
        parent?.let { getFavoritesUseCase.getFavorites(it, gender, { onFavoritesLoaded(it) }) }
    }

    fun manageBabyNameClick(parent: String, gender: NamesDataSource.Gender, babyName: BabyNameLikable) {
        saveFavoriteUseCase.saveFavoriteName(parent, gender, babyName.name)
        babyName.liked = !babyName.liked

        if (babyName.liked) {
            favoriteCounter++
        } else {
            favoriteCounter--
        }
        view?.updateFavoriteCounter(favoriteCounter)
    }

    fun onOkClicked() {
        val likedBabyNames = view?.getLikedBabyNames() ?: emptyList()
        if (likedBabyNames.isNotEmpty()) {
            view?.onLikedNamesChosen()
        }else {
            view?.showNoFavoritesMessage()
        }
    }

    fun onFavoritesLoaded(favoriteList: List<String>) {
        Logger.debug("onFavoritesLoaded: ${favoriteList.size} favorites")
        favoriteCounter = favoriteList.size
        view?.setFavoriteList(favoriteList)
        view?.updateFavoriteCounter(favoriteCounter)
    }

}
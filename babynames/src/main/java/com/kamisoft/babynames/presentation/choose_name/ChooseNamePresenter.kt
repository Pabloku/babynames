package com.kamisoft.babynames.presentation.choose_name

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.usecase.GetFavoriteList
import com.kamisoft.babynames.domain.usecase.SaveFavoriteName
import com.kamisoft.babynames.logger.Logger
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

class ChooseNamePresenter(
        private val getFavoritesUseCase: GetFavoriteList,
        private val saveFavoriteUseCase: SaveFavoriteName) :
        MvpBasePresenter<ChooseNameView>() {

    fun start() {
        val pullToRefresh = true
        view?.initViews()
        view?.showLoading(!pullToRefresh)
    }

    fun loadData() {
        Logger.debug("Future: loadData")
        launch(CommonPool) {
            Logger.debug("Future: loadData launch")
            if (ChooseNameFragment.listFuture.ready) {
                Logger.debug("Future: is ready")
                val result = ChooseNameFragment.listFuture.result ?: emptyList()
                view?.setData(result)
                view?.showContent()
            } else {
                Logger.debug("Future: not ready")
                val result = ChooseNameFragment.listFuture.await(30000) ?: emptyList()//TODO Ese 30000...
                view?.setData(result)
                view?.showContent()
            }
        }
    }

    fun loadFavorites(parent: String?, gender: NamesDataSource.Gender) {
        parent?.let { getFavoritesUseCase.getFavorites(it, gender, { onFavoritesLoaded(it) }) }
    }

    fun manageBabyNameClick(parent: String, gender: NamesDataSource.Gender, babyName: BabyName) {
        saveFavoriteUseCase.saveFavoriteName(parent, gender, babyName.name)
        babyName.liked = !babyName.liked
    }

    fun getLikedBabyNames(babyNameList: List<BabyName>): List<BabyName> {
        return babyNameList.filter { it.liked }
    }

    fun onFavoritesLoaded(favoriteList: List<String>) {
        view?.setFavoriteList(favoriteList)
    }

}
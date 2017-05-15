package com.kamisoft.babynames.presentation.choose_name

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.usecase.GetFavoriteList
import com.kamisoft.babynames.domain.usecase.GetNameList
import com.kamisoft.babynames.domain.usecase.SaveFavoriteName

class ChooseNamePresenter(private val getNamesUseCase: GetNameList,
                          private val getFavoritesUseCase: GetFavoriteList,
                          private val saveFavoriteUseCase: SaveFavoriteName) :
        MvpBasePresenter<ChooseNameView>() {

    private var parent: String? = null
    private var gender: NamesDataSource.Gender = NamesDataSource.Gender.MALE

    fun loadNames(gender: NamesDataSource.Gender) {
        getNamesUseCase.getNames(gender, NamesCallBack())
    }

    fun loadFavorites(parent: String?, gender: NamesDataSource.Gender) {
        parent?.let { getFavoritesUseCase.getFavorites(it, gender, FavoritesCallback()) }
    }

    fun manageBabyNameClick(parent: String, gender: NamesDataSource.Gender, babyName: BabyName) {
        saveFavoriteUseCase.saveFavoriteName(parent, gender, babyName.name)
        babyName.liked = !babyName.liked
    }

    fun getLikedBabyNames(babyNameList: List<BabyName>): List<BabyName> {
        return babyNameList.filter { it.liked }
    }

    fun onNamesLoaded(nameList: List<BabyName>) {
        loadFavorites(parent, gender)
        view?.setData(nameList)
        view?.showContent()
    }

    fun onFavoritesLoaded(favoriteList: List<String>) {
        view?.setFavoriteList(favoriteList)
    }

    inner class NamesCallBack : GetNameList.CallBacks.NamesCallback {
        override fun namesLoaded(nameList: List<BabyName>) {
            onNamesLoaded(nameList)
        }
    }

    inner class FavoritesCallback : GetFavoriteList.CallBacks.FavoritesCallback {
        override fun favoritesLoaded(favoriteList: List<String>) {
            onFavoritesLoaded(favoriteList)
        }
    }

}
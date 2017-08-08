package com.kamisoft.babynames.presentation.choose_name

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView
import com.kamisoft.babynames.presentation.model.BabyNameLikable

interface ChooseNameView : MvpLceView<List<BabyNameLikable>> {

    fun initViews()

    fun setFavoriteList(favorites: List<String>)

    fun updateFavoriteCounter(favoriteCount: Int)

    fun showNoFavoritesMessage()

    fun getLikedBabyNames(): List<BabyNameLikable>

    fun onLikedNamesChosen()

    fun showSearchView()

}